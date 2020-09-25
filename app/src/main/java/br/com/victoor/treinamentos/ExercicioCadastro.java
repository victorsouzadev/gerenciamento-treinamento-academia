package br.com.victoor.treinamentos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.victoor.treinamentos.adapter.ListExercicioAdapter;
import br.com.victoor.treinamentos.model.Exercicio;
import br.com.victoor.treinamentos.model.User;

public class ExercicioCadastro extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {
    private User user;
    String path;
    List<Exercicio> exercicios;
    RecyclerView recyclerView;
    ListExercicioAdapter adapter;
    private TextInputLayout textInputLayout;
    private AutoCompleteTextView editTextName;
    private List<String> fields;
    private ArrayAdapter<String> adapterName;
    private Spinner spinnerSeries;
    private int series = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercicio_cadastro);

        fields = new ArrayList<>();
        exercicios = new ArrayList<>();

        configToolbar();
        configSpinner();
        configAutoComplete();

        Intent intent = getIntent();
        this.user = (User) intent.getSerializableExtra("user");
        path =  intent.getStringExtra("path");


        getExercicios();



        configuraRecyclerView();


        ImageButton bntAddExercicio = findViewById(R.id.bnt_add_exercicio);
        bntAddExercicio.setOnClickListener(this);





    }

    private void configSpinner() {
        spinnerSeries = findViewById(R.id.spinner_series);
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,R.array.series,android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSeries.setAdapter(adapterSpinner);
        spinnerSeries.setOnItemSelectedListener(this);
    }

    private void configAutoComplete() {
        editTextName = findViewById(R.id.autoCompleteExercicio);
        textInputLayout = findViewById(R.id.layout_input_text);
        textInputLayout.setHint("Exercicio");

        listFields();
        adapterName = new ArrayAdapter<String>(ExercicioCadastro.this, android.R.layout.simple_list_item_1,fields);
        editTextName.setAdapter(adapterName);
    }

    private void listFields() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("exercicio");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //fields.clear();

                for (DataSnapshot value: dataSnapshot.getChildren()){
                    String name = value.getValue(String.class);
                    fields.add(name);
                }
                System.out.println("Fiels Notificado");
                adapterName.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }


    private void getExercicios() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(path+"/exercicio");
        System.out.println(path);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                exercicios.clear();
                System.out.println("heiii");
                System.out.println(dataSnapshot.getValue());
                for (DataSnapshot value: dataSnapshot.getChildren()){
                    Exercicio exercicio = value.getValue(Exercicio.class);
                    exercicio.setId(value.getKey());

                    System.out.println(exercicio);
                    exercicios.add(exercicio);
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
        recyclerView = findViewById(R.id.lista_exercicio);

        adapter = new ListExercicioAdapter(this, user, exercicios,path+"/exercicio");
        System.out.println(adapter);
        recyclerView.setAdapter(adapter);




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bnt_add_exercicio:{
                String nameExercicio = editTextName.getText().toString();

                ObserveEditToUpdateError(textInputLayout, editTextName);
                if(nameExercicio.trim().isEmpty()){
                    textInputLayout.setError("Campo vazio");
                }else{
                    Exercicio exercicio = new Exercicio();
                    saveName(nameExercicio);
                    exercicio.setName(nameExercicio);
                    exercicio.setRepeticoes(series);

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    database.getReference(path+"/exercicio").push().setValue(exercicio);
                    editTextName.getText().clear();
                    spinnerSeries.setId(0);
                    listFields();
                }



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

    private void configToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Exercício");
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
    private void saveName(String nameExercicio) {
        boolean save = true;
        for(String field: fields){
            if(field.equals(nameExercicio)){
                save = false;
            }
        }
        if(save){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.getReference("exercicio").push().setValue(nameExercicio);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        series = Integer.parseInt(parent.getItemAtPosition(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
