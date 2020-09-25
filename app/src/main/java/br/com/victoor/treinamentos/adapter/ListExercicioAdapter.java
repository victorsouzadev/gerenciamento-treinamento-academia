package br.com.victoor.treinamentos.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import br.com.victoor.treinamentos.ExercicioCadastro;
import br.com.victoor.treinamentos.R;
import br.com.victoor.treinamentos.model.Exercicio;
import br.com.victoor.treinamentos.model.Membro;
import br.com.victoor.treinamentos.model.User;

public class ListExercicioAdapter extends RecyclerView.Adapter<ListExercicioAdapter.RemoveViewHolderExercicio>  {
    private final User user;
    private final Context context;
    private final List<Exercicio> exercicios;
    private final String path;

    public ListExercicioAdapter(Context context, User users, List<Exercicio> exercicios,String path) {
        this.context = context;
        this.user = users;
        this.exercicios = exercicios;
        this.path = path;

    }

    @NonNull
    @Override
    public RemoveViewHolderExercicio onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_simple_remove_exercicio, parent, false);
        return new RemoveViewHolderExercicio(viewCriada);

    }

    @Override
    public void onBindViewHolder(@NonNull RemoveViewHolderExercicio holder, int position) {
        Exercicio membro = exercicios.get(position);
        holder.vincula(membro);
    }

    @Override
    public int getItemCount() {
        return exercicios.size();
    }

    class RemoveViewHolderExercicio extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView series;
        private final ImageView bntRemove;
        FirebaseDatabase database;
        Exercicio exercicio;
        public RemoveViewHolderExercicio(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.item_lista_value_exercicio);
            this.bntRemove = itemView.findViewById(R.id.bnt_remove_exercicio_item);
            this.series = itemView.findViewById(R.id.item_series);
            database = FirebaseDatabase.getInstance();

            this.bntRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    database.getReference(path+"/"+exercicio.getId()).removeValue();
                    exercicios.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    Intent intent = new Intent(context, ExercicioCadastro.class);
//                    intent.putExtra("user", user);
//                    intent.putExtra("path", path+"/"+ exercicio.getId() );
//                    context.startActivity(intent);
//                }
//            });
        }

        public void vincula(Exercicio exercicio) {
            name.setText(exercicio.getName());
            String series = "(" + exercicio.getRepeticoes() + " séries)";
            this.series.setText(exercicio.getRepeticoes()>1?series:"série única");
            this.exercicio = exercicio;
        }


    }
}

