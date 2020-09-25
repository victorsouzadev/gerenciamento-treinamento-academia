package br.com.victoor.treinamentos.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import br.com.victoor.treinamentos.MembroCadastro;
import br.com.victoor.treinamentos.R;
import br.com.victoor.treinamentos.model.Treino;
import br.com.victoor.treinamentos.model.User;
import br.com.victoor.treinamentos.utils.Base64Custom;

public class TreinoListAdapter extends RecyclerView.Adapter<TreinoListAdapter.RemoveViewHolder> {
    private final List<Treino> treinos;
    private final Context context;
    private final User user;


    public TreinoListAdapter(Context context, List<Treino> treinos,User user){
        this.treinos = treinos;
        this.context = context;
        this.user = user;
    }


    @NonNull
    @Override
    public RemoveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_simple_remove, parent, false);
        return new RemoveViewHolder(viewCriada);

    }

    @Override
    public void onBindViewHolder(@NonNull RemoveViewHolder holder, int position) {
        Treino treino = treinos.get(position);
        holder.vincula(treino);
    }


    @Override
    public int getItemCount() {
        return treinos.size();
    }



    class RemoveViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final ImageView bntRemove;
        FirebaseDatabase database;
        String path;
        public RemoveViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.item_lista_value);
            this.bntRemove = itemView.findViewById(R.id.bnt_remove_item);
            database = FirebaseDatabase.getInstance();

            this.bntRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    path = "user/" + Base64Custom.codificaBase64(user.getEmail()) + "/treinos/"+treinos.get(getAdapterPosition()).getId();
                    database.getReference(path).removeValue();
                    treinos.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    path = "user/" + Base64Custom.codificaBase64(user.getEmail()) + "/treinos/"+treinos.get(getAdapterPosition()).getId();
                    Intent intent = new Intent(context, MembroCadastro.class);
                    intent.putExtra("user", user);
                    intent.putExtra("path", path );
                    context.startActivity(intent);

                }
            });
        }

        public void vincula(Treino treino) {
            name.setText(treino.getName());
        }


    }
}
