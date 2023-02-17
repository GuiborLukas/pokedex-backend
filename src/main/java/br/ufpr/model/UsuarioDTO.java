package br.ufpr.model;

import java.io.Serializable;

public class UsuarioDTO implements Serializable {


	private static final long serialVersionUID = 1L;

	private long id;
	private String login;
	private String senha;

	public UsuarioDTO() {
		super();
	}

	public UsuarioDTO(long id, String login, String senha) {
		super();
		this.id = id;
		this.login = login;
		this.senha = senha;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
