package br.ufpr.model;

import java.io.Serializable;

public class Habilidade implements Comparable<Habilidade>, Serializable{

	private int quantidade;
	private String nome;
	
	public Habilidade() {
		super();
	}
	public Habilidade(int quantidade, String nome) {
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
	public int compareTo(Habilidade o) {
        if (this.quantidade < o.quantidade) {
            return -1;
        }
        if (this.quantidade > o.quantidade) {
            return 1;
        }
        return 0;
	}
	
}
