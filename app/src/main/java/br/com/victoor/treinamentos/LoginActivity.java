package br.com.victoor.treinamentos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import br.com.victoor.treinamentos.model.User;
import br.com.victoor.treinamentos.session.DataUser;
import br.com.victoor.treinamentos.utils.Base64Custom;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private GoogleSignInAccount account;
    //private ImageButton loginButton;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 0;
    ProgressDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        screen();


        //getSupportActionBar().hide();
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loading...");
        mDialog.setCancelable(false);


        findViewById(R.id.sign_in_button).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }

    private void screen() {


    }


    @Override
    protected void onStart() {
        super.onStart();
        account = GoogleSignIn.getLastSignedInAccount(this);
        System.out.println("Start");
        System.out.println(account);
        updateUI();
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
        final DatabaseReference myRef = database.getReference("user/"+Base64Custom.codificaBase64(account.getEmail()));

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
        mDialog.dismiss();
        Intent intent = new Intent(LoginActivity.this, UserAtivity.class);
        startActivity(intent);
    }




    private void startAdmin() {
        mDialog.dismiss();
        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                mDialog.show();
                signIn();
                break;

        }
    }

    private void signIn() {


        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {


        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI();

        } catch (ApiException e) {
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
            updateUI();
            mDialog.dismiss();
        }

    }
}






