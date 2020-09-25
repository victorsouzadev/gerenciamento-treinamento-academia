package br.com.victoor.treinamentos;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.victoor.treinamentos.adapter.ListUsersAdapter;
import br.com.victoor.treinamentos.model.User;
import br.com.victoor.treinamentos.session.DataUser;
import br.com.victoor.treinamentos.utils.Base64Custom;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    private GoogleSignInClient mGoogleSignInClient;
    private ListUsersAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<User> allUsers = new ArrayList<User>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        if(!DataUser.isAdmin()){
            Intent intent = new Intent(AdminActivity.this, UserAtivity.class);
            startActivity(intent);
        }

        receiveSessionUser();
        getUsers();
        configToolbar();

        configuraRecyclerView(allUsers);
        setTitle("Admin");


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminActivity.this, CadastroUser.class);
                startActivity(intent);


            }
        });


    }

    private void configToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Admin");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



    }

    private void getUsers(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                allUsers.clear();
                for (DataSnapshot value: dataSnapshot.getChildren()){
                    User user = value.getValue(User.class);
                    System.out.println(DataUser.getId());
                    if(!user.getEmail().equals(DataUser.getId())){
                        allUsers.add(user);
                    }

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



    private void configuraRecyclerView(List<User> allUsers) {
        recyclerView = findViewById(R.id.admin_lista_users);

        adapter = new ListUsersAdapter(this, allUsers);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void receiveSessionUser() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_exit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                signOut();
                Intent it = new Intent(this, LoginActivity.class);
                //it.putExtra("usuario", usuario);
                startActivity(it);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
    }
    @Override
    public void onBackPressed() {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // ...
            case R.id.logout:

                break;
            // ...
        }
    }

}
