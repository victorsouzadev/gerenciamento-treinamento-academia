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
import br.com.victoor.treinamentos.model.Membro;
import br.com.victoor.treinamentos.model.User;


public class ListMembrosAdapter extends RecyclerView.Adapter<ListMembrosAdapter.MembroViewHolder> {
    private final User user;
    private final Context context;
    private final List<Membro> membros;
    private final String path;

    public ListMembrosAdapter(Context context, User users, List<Membro> membros,String path) {
        this.context = context;
        this.user = users;
        this.membros = membros;
        this.path = path;

    }

    @NonNull
    @Override
    public ListMembrosAdapter.MembroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_simple_remove, parent, false);
        return new MembroViewHolder(viewCriada);

    }

    @Override
    public void onBindViewHolder(@NonNull MembroViewHolder holder, int position) {
        Membro membro = membros.get(position);
        holder.vincula(membro);
    }

    @Override
    public int getItemCount() {
        return membros.size();
    }

    class MembroViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final ImageView bntRemove;
        FirebaseDatabase database;
        Membro membro;
        public MembroViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.item_lista_value);
            this.bntRemove = itemView.findViewById(R.id.bnt_remove_item);
            database = FirebaseDatabase.getInstance();

            this.bntRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    database.getReference(path+"/"+membro.getId()).removeValue();
                    membros.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ExercicioCadastro.class);
                    intent.putExtra("user", user);
                    intent.putExtra("path", path+"/"+membro.getId() );
                    context.startActivity(intent);
                }
            });
        }

        public void vincula(Membro membro) {
            name.setText(membro.getName());
            this.membro = membro;
        }


    }
}




