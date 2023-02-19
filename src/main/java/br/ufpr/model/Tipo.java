package br.ufpr.model;

import java.io.Serializable;

public class Tipo implements Comparable<Tipo>, Serializable{

	private int quantidade;
	private String nome;
	
	public Tipo() {
		super();
	}
	public Tipo(int quantidade, String nome) {
		super();
		this.quantidade = quantidade;
		this.nome = nome;
	}
	public int getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public int compareTo(Tipo o) {
        if (this.quantidade < o.quantidade) {
            return -1;
        }
        if (this.quantidade > o.quantidade) {
            return 1;
        }
        return 0;
	}
	
}
