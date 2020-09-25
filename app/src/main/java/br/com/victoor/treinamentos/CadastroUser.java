package br.com.victoor.treinamentos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.victoor.treinamentos.model.User;
import br.com.victoor.treinamentos.utils.Base64Custom;
import br.com.victoor.treinamentos.utils.ValidationFields;

public class CadastroUser extends AppCompatActivity implements View.OnClickListener {


    TextInputEditText nameInput;
    TextInputEditText emailInput;
    TextInputLayout nameUserLayout;
    TextInputLayout emailUserLayout;
    Button cadastrarUser;
    User user;
    boolean updateUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_user);


        nameInput = findViewById(R.id.nomeUserInput);
        emailInput = findViewById(R.id.emailUserInput);
        nameUserLayout = findViewById(R.id.nomeUser);
        emailUserLayout = findViewById(R.id.emailUser);

        cadastrarUser = findViewById(R.id.cadastrarUser);
        cadastrarUser.setOnClickListener(this);

        Intent intent = getIntent();
        String action_text = "Cadastrar";
        if(intent.getSerializableExtra("user")!=null){
            this.user = (User) intent.getSerializableExtra("user");
            action_text = "Atualizar";
            cadastrarUser.setText(action_text);
            emailInput.setVisibility(View.GONE);
            updateUser = true;

            configToolbar(action_text);
        }
        configToolbar(action_text);




    }


    private void configToolbar(String action) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle(action);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(updateUser){
                    intent = new Intent(CadastroUser.this, MenuUserGerenciamento.class);
                }else{
                    intent = new Intent(CadastroUser.this, AdminActivity.class);
                }

                intent.putExtra("user", user);
                startActivity(intent);

            }
        });

        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.write), PorterDuff.Mode.SRC_ATOP);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cadastrarUser:{


                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("user");

                ValidationFields.ObserveEditToUpdateError(emailUserLayout,emailInput);

                ValidationFields.ObserveEditToUpdateError(nameUserLayout,nameInput);
                if(updateUser){
                    boolean name = ValidationFields.fieldIsEmpty(nameUserLayout,nameInput);
                    if(!name){
                        myRef.child(Base64Custom.codificaBase64(user.getEmail())+"/name").setValue(nameInput.getText().toString());
                        finish();
                    }

                }else {
                    boolean email = ValidationFields.fieldIsEmpty(emailUserLayout,emailInput);
                    boolean name = ValidationFields.fieldIsEmpty(nameUserLayout,nameInput);



                    if(!name && !email){
                        User user = new User(nameInput.getText().toString(),updateUser ? this.user.getEmail():emailInput.getText().toString());
                        myRef.child(Base64Custom.codificaBase64(user.getEmail())).setValue(user);
                        if(updateUser){
                            Intent intent = new Intent(this, MenuUserGerenciamento.class);
                            intent.putExtra("user", user);
                            startActivity(intent);
                        }
                        finish();
                    }

                }





            }
        }
    }

}
