package br.ufpr.model;

import java.util.List;

public class PokemonDTO {
	
	private Long id;
	private String nome;
	private String tipo;
	private List<String> habilidade;
	private byte[] foto;
	private Long usuario;
	
	public PokemonDTO() {
		super();
	}

	public PokemonDTO(Long id, String nome, String tipo, List<String> habilidade, byte[] foto, Long usuario) {
		super();
		this.id = id;
		this.nome = nome;
		this.tipo = tipo;
		this.habilidade = habilidade;
		this.foto = foto;
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
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public List<String> getHabilidade() {
		return habilidade;
	}

	public void setHabilidade(List<String> habilidade) {
		this.habilidade = habilidade;
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public Long getUsuario() {
		return usuario;
	}

	public void setUsuario(Long usuario) {
		this.usuario = usuario;
	}
	
}
