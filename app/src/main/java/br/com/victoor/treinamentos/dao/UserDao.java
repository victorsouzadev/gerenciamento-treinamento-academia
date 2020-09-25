package br.com.victoor.treinamentos.dao;

import com.google.firebase.firestore.FirebaseFirestore;

import br.com.victoor.treinamentos.model.User;

public class UserDao {

    FirebaseFirestore db;
    private boolean result;

    public  void Insert(User user){
        db = FirebaseFirestore.getInstance();

    }

    public boolean getUserId(java.lang.String id){
        return true;
    }


}
