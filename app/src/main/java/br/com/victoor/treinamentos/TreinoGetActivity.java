package br.com.victoor.treinamentos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import br.com.victoor.treinamentos.adapter.AdapterTabTreino;

public class TreinoGetActivity extends AppCompatActivity {

    TabLayout tabLayout;
    AdapterTabTreino adapter;
    String[] tabs = {"Treino 1", "Treino 2", "Treino 3"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treino_get);
        configElements();


    }
    private void configElements() {
//        ViewPager viewPager = findViewById(R.id.pagerTreino);
//        adapter = new AdapterTabTreino(getSupportFragmentManager(), tabs);
//        viewPager.setAdapter(adapter);
//
//        tabLayout = findViewById(R.id.tabTreino);
//        tabLayout.setupWithViewPager(viewPager);


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
                //Intent it = new Intent(this, LoginActivity.class);
                //it.putExtra("usuario", usuario);
                //startActivity(it);
                //tabLayout.addTab(tabLayout.newTab().);
                //configElements();
                //tabLayout.
                Toast.makeText(this, "add tab", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
