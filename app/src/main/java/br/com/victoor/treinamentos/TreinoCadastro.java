package br.com.victoor.treinamentos;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.victoor.treinamentos.adapter.TreinoListAdapter;
import br.com.victoor.treinamentos.model.Treino;
import br.com.victoor.treinamentos.model.User;
import br.com.victoor.treinamentos.utils.Base64Custom;


public class TreinoCadastro extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    ArrayList<Treino> treinos = new ArrayList<>();
    private TreinoListAdapter adapter;
    private static int count;
    private Button bntAddTreino;
    private User user;
    private Button bntGeneratePDF;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treino_cadastro);

        configToolbar();


        database = FirebaseDatabase.getInstance();
        Intent intent = getIntent();
        this.user = (User) intent.getSerializableExtra("user");

        configuraRecyclerView();
        getTreino();


        bntAddTreino = findViewById(R.id.bnt_add_treino);
        bntAddTreino.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void configToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Treino");
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


    private void getTreino() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user/"+ Base64Custom.codificaBase64(user.getEmail())+"/treinos");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                treinos.clear();
                System.out.println(dataSnapshot.getRef());
                for (DataSnapshot value: dataSnapshot.getChildren()){
                    System.out.println(value);
                    Treino treino = value.getValue(Treino.class);
                    treino.setId(value.getKey());

                    System.out.println(treino);
                    treinos.add(treino);
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

        adapter = new TreinoListAdapter(this,treinos,user);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bnt_add_treino:{
                AlertDialog.Builder builder = new AlertDialog.Builder(TreinoCadastro.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_selecionar_name,null);
                final TextInputLayout textInputLayout = mView.findViewById(R.id.layout_input_text);
                final AutoCompleteTextView editTextName = mView.findViewById(R.id.cadastro_treino_name);
                textInputLayout.setHint("Treino");




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

                        if(isTextEmpty(editTextName.getText().toString())){
                            textInputLayout.setError("Campo vazio");
                        }else{
                            Treino treino = new Treino();
                            treino.setName("Treino "+editTextName.getText().toString());

                            database.getReference("user/" + Base64Custom.codificaBase64(user.getEmail()) + "/treinos").push().setValue(treino);
                            dialog.dismiss();
                        }
                    }
                });



                break;
            }

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

    private boolean isTextEmpty(String texto) {
        if(texto.trim().isEmpty()){
            return true;
        }else{
            return false;
        }

    }


}
