package br.ufpr.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufpr.model.Pokemon;

public interface UsuarioRepository extends JpaRepository<Pokemon, Long> {

	Optional<Pokemon> findByNome(String nome);

}