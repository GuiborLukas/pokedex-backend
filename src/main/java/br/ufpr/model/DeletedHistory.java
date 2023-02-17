package br.ufpr.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "tb_deletados", uniqueConstraints = { @UniqueConstraint(columnNames = { "nome_history" }) })
public class DeletedHistory implements Serializable{

	@Id
	@Column(name = "id_history")
	private Long id;
	
	@Column(name = "nome_history")
	private String nome;
	
	@Column(name = "usuario_history")
	private Long usuario;

	public DeletedHistory() {
		super();
	}

	public DeletedHistory(Long id, String nome, Long usuario) {
		super();
		this.id = id;
		this.nome = nome;
		this.usuario = usuario;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getUsuario() {
		return usuario;
	}

	public void setUsuario(Long usuario) {
		this.usuario = usuario;
	}
	
}
