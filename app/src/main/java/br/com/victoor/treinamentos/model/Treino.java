package br.com.victoor.treinamentos.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;

public class Treino implements Serializable {
    private String id;
    private String name;
    private ArrayList<Membro> membros;

    public Treino(String name, ArrayList<Membro> membros) {
        this.name = name;
        this.membros = new ArrayList<>();
    }

    public Treino() {
        this.membros = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Exclude
    public ArrayList<Membro> getMembros() {
        return membros;
    }

    public void setMembros(ArrayList<Membro> membros) {
        this.membros = membros;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Treino{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", membros=" + membros +
                '}';
    }
}
