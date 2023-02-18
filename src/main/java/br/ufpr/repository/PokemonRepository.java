package br.ufpr.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufpr.model.Pokemon;

public interface PokemonRepository extends JpaRepository<Pokemon, Long> {

	Optional<Pokemon> findByNome(String nome);
	
	Optional<Pokemon> findByTipo(String tipo);
	
	List<Pokemon> findAllByTipo(String tipo);

}
