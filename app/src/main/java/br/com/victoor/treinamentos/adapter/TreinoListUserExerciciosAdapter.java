package br.com.victoor.treinamentos.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import br.com.victoor.treinamentos.model.User;

public class TreinoListUserExerciciosAdapter extends RecyclerView.Adapter<TreinoListUserExerciciosAdapter.RemoveViewHolderExercicio>  {

    private final Context context;
    private final List<Exercicio> exercicios;


    public TreinoListUserExerciciosAdapter(Context context, List<Exercicio> exercicios) {
        this.context = context;
        this.exercicios = exercicios;


    }

    @NonNull
    @Override
    public TreinoListUserExerciciosAdapter.RemoveViewHolderExercicio onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_descricao_exercicios, parent, false);

        return new TreinoListUserExerciciosAdapter.RemoveViewHolderExercicio(viewCriada);

    }

    @Override
    public void onBindViewHolder(@NonNull TreinoListUserExerciciosAdapter.RemoveViewHolderExercicio holder, int position) {
        Exercicio membro = exercicios.get(position);
        holder.vincula(membro);
    }

    @Override
    public int getItemCount() {
        return exercicios.size();
    }

    class RemoveViewHolderExercicio extends RecyclerView.ViewHolder {

        private final TextView name;
        private final ImageView bntRemove;
        FirebaseDatabase database;
        Exercicio exercicio;


        public RemoveViewHolderExercicio(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name_exercicio_user);
            this.bntRemove = itemView.findViewById(R.id.bnt_remove_exercicio_item);


            database = FirebaseDatabase.getInstance();





        }


        public void vincula(Exercicio exercicio) {

            String series = "(" + exercicio.getRepeticoes() + " séries)";
            name.setText("- "+exercicio.getName()+" "+(exercicio.getRepeticoes()>1?series:"série única"));
            this.exercicio = exercicio;
        }


    }
}
