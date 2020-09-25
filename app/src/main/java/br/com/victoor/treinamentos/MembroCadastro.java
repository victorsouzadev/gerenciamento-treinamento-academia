package br.com.victoor.treinamentos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.victoor.treinamentos.adapter.ListMembrosAdapter;
import br.com.victoor.treinamentos.model.Membro;
import br.com.victoor.treinamentos.model.Treino;
import br.com.victoor.treinamentos.model.User;
import br.com.victoor.treinamentos.utils.Base64Custom;

public class MembroCadastro extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ListMembrosAdapter adapter;
    private User user;
    String path;
    private Button bntAddMembro;
    private List<Membro> membros;
    private List<String> fields;
    private ArrayAdapter<String> adapterName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membro_cadastro);

        bntAddMembro = findViewById(R.id.bnt_add_membro);
        this.bntAddMembro.setOnClickListener(this);

        configToolbar();

        fields = new ArrayList<>();
        membros = new ArrayList<>();
        Intent intent = getIntent();
        this.user = (User) intent.getSerializableExtra("user");
        path =  intent.getStringExtra("path");

        getMembros();
        configuraRecyclerView();
    }

    private void configToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Região");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.write), PorterDuff.Mode.SRC_ATOP);
    }


    private void getMembros() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(path+"/membros");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                membros.clear();

                for (DataSnapshot value: dataSnapshot.getChildren()){
                    Membro membro = value.getValue(Membro.class);
                    membro.setId(value.getKey());

                    System.out.println(membro);
                    membros.add(membro);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });


    }

    private void configuraRecyclerView() {

        recyclerView = findViewById(R.id.treino_cadastro_list_exercicios);
        adapter = new ListMembrosAdapter(this, user, membros,path+"/membros");

        recyclerView.setAdapter(adapter);



    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.bnt_add_membro){
            AlertDialog.Builder builder = new AlertDialog.Builder(MembroCadastro.this);
            View mView = getLayoutInflater().inflate(R.layout.dialog_selecionar_name,null);
            final TextInputLayout textInputLayout = mView.findViewById(R.id.layout_input_text);
            final AutoCompleteTextView editTextName = mView.findViewById(R.id.cadastro_treino_name);
            editTextName.setHint("Região");

            listFields();
            adapterName = new ArrayAdapter<String>(MembroCadastro.this, android.R.layout.simple_list_item_1,fields);
            editTextName.setAdapter(adapterName);


            builder.setPositiveButton("Ok", null);
            builder.setNegativeButton("Cancelar", null);


            ObserveEditToUpdateError(textInputLayout, editTextName);



            builder.setView(mView);
            final AlertDialog dialog = builder.create();
            dialog.show();
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(editTextName.getText().toString().trim().isEmpty()){
                        textInputLayout.setError("Campo vazio");
                    }else{
                        String nameMembro = editTextName.getText().toString();
                        Membro membro = new Membro();
                        saveName(nameMembro);
                        membro.setName(nameMembro);
                        setMembro(membro);
                        dialog.dismiss();
                    }
                }
            });





        }
    }
    private void ObserveEditToUpdateError(final TextInputLayout inputLayout, AutoCompleteTextView editTex) {
        editTex.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(inputLayout.getError()!=null){
                    inputLayout.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void listFields() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("membros");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                fields.clear();

                for (DataSnapshot value: dataSnapshot.getChildren()){
                    String name = value.getValue(String.class);
                    fields.add(name);
                }
                adapterName.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    private void saveName(String nameMembro) {
        boolean save = true;
        for(String field: fields){
            if(field.equals(nameMembro)){
                save = false;
            }
        }
        if(save){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.getReference("membros").push().setValue(nameMembro);
        }

    }

    private void setMembro(Membro membro) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference(path+"/membros").push().setValue(membro);

    }
}
