package br.com.victoor.treinamentos.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.victoor.treinamentos.MenuUserGerenciamento;
import br.com.victoor.treinamentos.R;
import br.com.victoor.treinamentos.TreinoCadastro;
import br.com.victoor.treinamentos.model.User;

public class ListUsersAdapter extends RecyclerView.Adapter<ListUsersAdapter.UserViewHolder> {
    private final List<User> users;
    private final Context context;

    public ListUsersAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;

    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_lista_user, parent, false);
        return new UserViewHolder(viewCriada);

    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User nota = users.get(position);
        holder.vincula(nota);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.item_lista_user);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        Intent intent = new Intent(context, MenuUserGerenciamento.class);
                        intent.putExtra("user", users.get(getAdapterPosition()));
                        context.startActivity(intent);




                }
            });
        }

        public void vincula(User user) {
            name.setText(user.getName());
        }


    }
}
