package br.com.victoor.treinamentos.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.victoor.treinamentos.R;
import br.com.victoor.treinamentos.model.Exercicio;
import br.com.victoor.treinamentos.model.Membro;
import br.com.victoor.treinamentos.model.Treino;

public class TreinoListUserAdapter extends RecyclerView.Adapter<TreinoListUserAdapter.RemoveViewHolder> {
    private final List<Membro> membros;
    private final Context context;
    private final String path;



    public TreinoListUserAdapter(Context context, List<Membro> treinos, String path){
        this.membros = treinos;
        this.context = context;
        this.path = path;

    }


    @NonNull
    @Override
    public RemoveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_user_exercicios, parent, false);
        return new RemoveViewHolder(viewCriada);

    }

    @Override
    public void onBindViewHolder(@NonNull RemoveViewHolder holder, int position) {
        Membro membro = membros.get(position);
        holder.vincula(membro);
    }


    @Override
    public int getItemCount() {
        return membros.size();
    }



    class RemoveViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final RecyclerView recyclerView;
        FirebaseDatabase database;
        Membro membro;

        List<Exercicio> exercicios= new ArrayList<>();
        TreinoListUserExerciciosAdapter adapter;
        public RemoveViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name_membro_user);
            database = FirebaseDatabase.getInstance();
            recyclerView = itemView.findViewById(R.id.descricao_exercicios);



            adapter = new TreinoListUserExerciciosAdapter(context, exercicios);
            recyclerView.setAdapter(adapter);



        }
        private void getExercicios() {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(path+"/"+membro.getId()+"/exercicio");
            System.out.println(path+membro.getId()+"/exercicio");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.

                    exercicios.clear();

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
        public void vincula(Membro membro) {
            this.membro = membro;
            name.setText(membro.getName());
            getExercicios();
        }


    }
}
