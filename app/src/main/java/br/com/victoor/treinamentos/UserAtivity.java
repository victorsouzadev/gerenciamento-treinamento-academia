package br.com.victoor.treinamentos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.victoor.treinamentos.adapter.AdapterTabTreino;
import br.com.victoor.treinamentos.model.Treino;
import br.com.victoor.treinamentos.session.DataUser;
import br.com.victoor.treinamentos.utils.Base64Custom;


public class UserAtivity extends AppCompatActivity implements View.OnClickListener {

    private GoogleSignInClient mGoogleSignInClient;
    TabLayout tabLayout;
    AdapterTabTreino adapter;

    private List<Treino> tabsTreinos;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_user);
        if(DataUser.isAdmin()){
            Intent intent = new Intent(UserAtivity.this, AdminActivity.class);
            startActivity(intent);
        }

        tabsTreinos = new ArrayList<>();
        getTreino();
        configToolbar();
        configElements();
        receiveSessionUser();


    }

    private void getTreino() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        System.out.println( DataUser.getPersonEmail());
        DatabaseReference myRef = database.getReference("user/"+Base64Custom.codificaBase64(DataUser.getId())+"/treinos");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                tabsTreinos.clear();

                for (DataSnapshot value: dataSnapshot.getChildren()){
                    Treino treino = value.getValue(Treino.class);
                    treino.setId(value.getKey());

                    System.out.println(treino);
                    tabsTreinos.add(treino);
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
    private void configElements() {
        ViewPager viewPager = findViewById(R.id.pagerTreino);
        adapter = new AdapterTabTreino(getSupportFragmentManager(),this, tabsTreinos);
        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tabTreino);
        tabLayout.setupWithViewPager(viewPager);


    }

    private void configToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Ficha de treino");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.write), PorterDuff.Mode.SRC_ATOP);
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
    public void onClick(View v) {
        switch (v.getId()) {
            // ...
            case R.id.logout:

                break;
            // ...
        }
    }

}