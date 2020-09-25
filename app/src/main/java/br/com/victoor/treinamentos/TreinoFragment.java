package br.com.victoor.treinamentos;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.victoor.treinamentos.adapter.TreinoListAdapter;
import br.com.victoor.treinamentos.adapter.TreinoListUserAdapter;
import br.com.victoor.treinamentos.model.Membro;
import br.com.victoor.treinamentos.model.Treino;
import br.com.victoor.treinamentos.model.User;
import br.com.victoor.treinamentos.utils.Base64Custom;


/**
 * A simple {@link Fragment} subclass.
 */
public class TreinoFragment extends Fragment {
    String path;
    ArrayList<Membro> membros = new ArrayList<>();
    private TreinoListUserAdapter adapter;
    private RecyclerView recyclerView;

    public TreinoFragment(Context context, String path) {
        // Required empty public constructor
        this.path = path;
        System.out.println(path);
//        Toast.makeText(context, path, Toast.LENGTH_SHORT).show();
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
                System.out.println(membros);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });


    }
    private void configuraRecyclerView(View view) {

        recyclerView = view.findViewById(R.id.fragment_list_treino);
        System.out.println(recyclerView);
        adapter = new TreinoListUserAdapter(getActivity(),membros,path+"/membros");
        recyclerView.setAdapter(adapter);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_treino, container, false);


        getMembros();
        configuraRecyclerView(view);







        return view;
    }
}
