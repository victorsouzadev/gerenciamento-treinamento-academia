package br.com.victoor.treinamentos.model;

import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String id;

    private String name;
    private String email;
    private List<Treino> treinos;

    private boolean admin;


    public User() {
        treinos = new ArrayList<>();
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.treinos = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
    @Exclude
    public List<Treino> getTreinos() {
        return treinos;
    }

    public void setTreinos(List<Treino> treinos) {
        this.treinos = treinos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", treinos=" + treinos +
                ", admin=" + admin +
                '}';
    }
}
