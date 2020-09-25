package br.com.victoor.treinamentos.model;

import java.io.Serializable;

public class Exercicio implements Serializable {
    private String id;
    private String name;
    private int repeticoes;

    public Exercicio(String name, int repeticoes) {
        this.name = name;
        this.repeticoes = repeticoes;
    }

    public Exercicio() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRepeticoes() {
        return repeticoes;
    }

    public void setRepeticoes(int repeticoes) {
        this.repeticoes = repeticoes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Exercicio{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", repeticoes=" + repeticoes +
                '}';
    }
}
