package br.ufpr.rest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.ufpr.exception.CustomException;
import br.ufpr.model.Login;
import br.ufpr.model.Usuario;
import br.ufpr.model.UsuarioDTO;
import br.ufpr.repository.UsuarioRepository;

@CrossOrigin
@RestController
public class AuthREST {

	@Autowired
	private UsuarioRepository repo;

	@Autowired
	private ModelMapper mapper;

	@PostMapping("/login")
	public ResponseEntity<UsuarioDTO> login(@RequestBody Login login) {
		String usuario = login.getLogin();
		String senha = login.getSenha();
		
		Optional<Usuario> user = repo.findByLogin(usuario);
		if(user.isEmpty()) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Usuário ou senha incorretos!");
		}else if (user.get().getSenha().equals(senha)){
			return ResponseEntity.status(HttpStatus.OK).body(mapper.map(user, UsuarioDTO.class));
		}else {
			throw new CustomException(HttpStatus.NOT_FOUND, "Usuário ou senha incorretos!");
		}
	}

	@PostMapping("/usuarios")
	public ResponseEntity<UsuarioDTO> inserir(@RequestBody UsuarioDTO usuario) {
		String username = usuario.getLogin();
		
		Optional<Usuario> user = repo.findByLogin(username);
		
		if(user.isPresent()) {
			throw new CustomException(HttpStatus.CONFLICT, "Usuário já existe!");
		}else {
			repo.save(mapper.map(usuario, Usuario.class));
			Optional<Usuario> usu = repo.findByLogin(usuario.getLogin());
			return ResponseEntity.status(HttpStatus.CREATED).body(mapper.map(usu, UsuarioDTO.class));
		}
	}
	
	@GetMapping("/usuarios")
	public ResponseEntity<List<UsuarioDTO>> listarTodos() {
		
		List<Usuario> lista = repo.findAll();
		
		if(lista.isEmpty()) {
			
			throw new CustomException(HttpStatus.NOT_FOUND, "Nenhum usuário encontrado!");
		}else {
			return ResponseEntity.status(HttpStatus.OK).body(lista.stream().map(e -> mapper.map(e, UsuarioDTO.class)).collect(Collectors.toList()));
		}
	}
	
	@GetMapping("/usuarios/{id}")
	public ResponseEntity<UsuarioDTO> listaUsuario(@PathVariable Long id){
		
		Optional<Usuario> user = repo.findById(id);
		if(user.isEmpty()) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Usuário não encontrado!");
		}else {
			return ResponseEntity.status(HttpStatus.OK).body(mapper.map(user, UsuarioDTO.class));
		}
	}
	
	@PutMapping("/usuarios/{id}")
	public ResponseEntity<UsuarioDTO> atualizar(@PathVariable Long id, @RequestBody UsuarioDTO usuario){
		
		Optional<Usuario> user = repo.findById(id);
		if(user.isEmpty()) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Usuário não encontrado!");
		}else {
			usuario.setId(id);
			repo.save(mapper.map(usuario, Usuario.class));
			user = repo.findById(id);
			return ResponseEntity.status(HttpStatus.OK).body(mapper.map(user, UsuarioDTO.class));
		}
	}
	
	@DeleteMapping("/usuarios/{id}")
	public ResponseEntity deleteUsuario(@PathVariable Long id){
		
		Optional<Usuario> usuario = repo.findById(id);
		if(usuario.isEmpty()) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Usuário não encontrado!");
		}else {
			repo.delete(mapper.map(usuario, Usuario.class));
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
	}
}