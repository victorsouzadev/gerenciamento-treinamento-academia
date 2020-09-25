package br.com.victoor.treinamentos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.victoor.treinamentos.model.User;
import br.com.victoor.treinamentos.session.DataUser;
import br.com.victoor.treinamentos.utils.Base64Custom;

public class Screen extends AppCompatActivity {
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_screen);
        super.onCreate(savedInstanceState);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        account = GoogleSignIn.getLastSignedInAccount(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(account==null){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                    Intent screenIntent = new Intent(Screen.this,LoginActivity.class);
                    startActivity(screenIntent);
                    finish();
            }
        },4000);
        }else{
            updateUI();
        }
    }

    private void updateUI() {


        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account !=null){
            new DataUser(account);
            isUserRegister(account);
        }

    }

    private void isUserRegister(final GoogleSignInAccount account) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("user/"+ Base64Custom.codificaBase64(account.getEmail()));

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                System.out.println(dataSnapshot.getValue());
                User value = dataSnapshot.getValue(User.class);

                Log.d("TAG", "Value is: " + value);

                if(value!=null){
                    //Map<java.lang.String, Object> data = getDataUnique(task);
                    DataUser.setId(value.getEmail());
                    DataUser.setAdmin(value.isAdmin());
                    DataUser.setPersonEmail(value.getEmail());

                    defineRedirection();
                    System.out.println("Aqui");
                }else{
                    register(account);
                    isUserRegister(account);

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });


    }

    private void register(GoogleSignInAccount account) {

        User user = new User(account.getGivenName(),account.getEmail());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user");

        myRef.child(Base64Custom.codificaBase64(user.getEmail())).setValue(user);


    }

    private void defineRedirection() {

        if(DataUser.isAdmin()){
            startAdmin();
        }else{
            startUser();
        }

    }

    private void startUser() {

        Intent intent = new Intent(Screen.this, UserAtivity.class);
        startActivity(intent);
        finish();
    }




    private void startAdmin() {

        Intent intent = new Intent(Screen.this, AdminActivity.class);
        startActivity(intent);
        finish();
    }
}
