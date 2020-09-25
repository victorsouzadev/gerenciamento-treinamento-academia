package br.com.victoor.treinamentos.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Membro implements Serializable {
    private String id;
    private String name;
    private ArrayList<Exercicio> exercicios;
    private boolean expanded;

    @Override
    public String toString() {
        return "Membro{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", exercicios=" + exercicios +
                ", expanded=" + expanded +
                '}';
    }

    public Membro(String name) {
        this.name = name;
        this.exercicios = new ArrayList<>();
        this.expanded = false;
    }

    public Membro() {
        this.exercicios = new ArrayList<>();
        this.expanded = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public ArrayList<Exercicio> getExercicios() {
        return exercicios;
    }

    public void setExercicios(ArrayList<Exercicio> exercicios) {
        this.exercicios = exercicios;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
