package br.ufpr.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils; 

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
import br.ufpr.model.DeletedHistory;
import br.ufpr.model.Habilidade;
import br.ufpr.model.Pokemon;
import br.ufpr.model.PokemonDTO;
import br.ufpr.model.Tipo;
import br.ufpr.repository.DeletedHistoryRepository;
import br.ufpr.repository.PokemonRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;

@CrossOrigin
@RestController

public class PokemonREST {

	@Autowired
	private PokemonRepository repo;

	@Autowired
	private DeletedHistoryRepository dhRepo;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@GetMapping("/pokemons")
	public ResponseEntity<List<PokemonDTO>> obterTodosPokemons() {

		List<Pokemon> lista = repo.findAll();

		if (lista.isEmpty()) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Nenhum pokemon encontrado!");
		} else {
			return ResponseEntity.status(HttpStatus.OK)
					.body(lista.stream().map(e -> mapper.map(e, PokemonDTO.class)).collect(Collectors.toList()));
		}
	}

	@GetMapping("/pokemons/habilidades/{habilidade}")
	public ResponseEntity<List<PokemonDTO>> buscarPorHabilidade(@PathVariable String habilidade) {

		List<Pokemon> lista = repo.findAll();

		lista = filtrarPorHabilidade(lista, habilidade);

		if (lista.isEmpty()) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Nenhum pokemon encontrado!");
		} else {
			return ResponseEntity.status(HttpStatus.OK)
					.body(lista.stream().map(e -> mapper.map(e, PokemonDTO.class)).collect(Collectors.toList()));
		}
	}
	
	@GetMapping("/pokemons/tipos/{tipo}")
	public ResponseEntity<List<PokemonDTO>> buscarPorTipo(@PathVariable String tipo) {

		List<Pokemon> lista = repo.findAll();

		lista = filtrarPorTipo(lista, tipo);
		
		if (lista.isEmpty()) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Nenhum pokemon encontrado!");
		} else {
			return ResponseEntity.status(HttpStatus.OK)
					.body(lista.stream().map(e -> mapper.map(e, PokemonDTO.class)).collect(Collectors.toList()));
		}
	}

	@GetMapping("/pokemons/{id}")
	public ResponseEntity<PokemonDTO> buscaPorId(@PathVariable Long id) {

		Optional<Pokemon> pokemon = repo.findById(id);
		if (pokemon.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(mapper.map(pokemon, PokemonDTO.class));
		}
	}

	@PostMapping("/pokemons")
	@Transactional
	public ResponseEntity<PokemonDTO> inserirPokemon(@RequestBody PokemonDTO pokemon) {
		Optional<Pokemon> poke = repo.findByNome(pokemon.getNome());
		Optional<DeletedHistory> dh = dhRepo.findByNome(pokemon.getNome());
		
		if (poke.isPresent()) {
			throw new CustomException(HttpStatus.CONFLICT, "Pokemon j?? cadastrado!");
		} else {
			if(dh.isPresent()) {
				pokemon.setUsuario(dh.get().getUsuario());
				dhRepo.delete(mapper.map(dh, DeletedHistory.class));
			}
			
			try {
				EntityManager entityManager = entityManagerFactory.createEntityManager();
				entityManager.getTransaction().begin();
				entityManager.persist(mapper.map(pokemon, Pokemon.class));
				entityManager.getTransaction().commit();
			} catch (Exception e) {
				throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao inserir o Pokemon!");
			}
			poke = repo.findByNome(pokemon.getNome());
			return ResponseEntity.status(HttpStatus.OK).body(mapper.map(poke, PokemonDTO.class));
		}
	}

	@PutMapping("/pokemons/{id}")
	@Transactional
	public ResponseEntity<PokemonDTO> alterarPokemon(@PathVariable("id") long id, @RequestBody Pokemon pokemon) {
		Optional<Pokemon> poke = repo.findById(id);
		
		
		
		if (poke.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} else {
			try {
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			entityManager.find(Pokemon.class, id);
			entityManager.merge(pokemon);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao alterar o Pokemon!");
		}
		poke = repo.findByNome(pokemon.getNome());
		return ResponseEntity.status(HttpStatus.OK).body(mapper.map(poke, PokemonDTO.class));
		}

	}

	@DeleteMapping("/pokemons/{id}")
	public ResponseEntity<String> removerPokemon(@PathVariable("id") long id) {

		Optional<Pokemon> pokemon = repo.findById(id);
		if (pokemon.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} else {
			DeletedHistory dh = new DeletedHistory();
			dh.setId(pokemon.get().getId());
			dh.setNome(pokemon.get().getNome());
			dh.setUsuario(pokemon.get().getUsuario());
			dhRepo.save(mapper.map(dh, DeletedHistory.class));
			repo.delete(mapper.map(pokemon, Pokemon.class));
			return ResponseEntity.status(HttpStatus.OK).body("Pokemon removido com sucesso!");
		}
	}

	public static List<Pokemon> filtrarPorHabilidade(List<Pokemon> pokemons, String habilidade) {
		List<Pokemon> newList = new ArrayList<>();
		for (Pokemon p : pokemons) {
			for (String h : p.getHabilidade()) {
				String hn = StringUtils.stripAccents(h);
				String habilidaden = StringUtils.stripAccents(habilidade);
				if (habilidaden.equalsIgnoreCase(hn) || hn.toLowerCase().contains(habilidaden.toLowerCase())) {
					newList.add(p);
					break;
				}
			}
		}

		return newList;
	}

	public static List<Pokemon> filtrarPorTipo(List<Pokemon> pokemons, String tipo) {
		List<Pokemon> newList = new ArrayList<>();
		for (Pokemon p : pokemons) {
				String tn = StringUtils.stripAccents(p.getTipo());
				String tipon = StringUtils.stripAccents(tipo);
				if (tipo.equalsIgnoreCase(tn) || tn.toLowerCase().contains(tipon.toLowerCase())) {
					newList.add(p);
				}
			}
		return newList;
	}
	
	@GetMapping("/pokemons/habilidades/toptres")
	public ResponseEntity<List<Habilidade>> topTresHabilidades() {
		List<Pokemon> pokemons = repo.findAll();
		List<Habilidade> lista = new ArrayList<>();
		for (Pokemon p : pokemons) {		
			for (String h : p.getHabilidade()) {
				boolean found = false;
				if(h == null || h.equals("")) {
					continue;
				}
				if(lista.isEmpty()) {
					lista.add(new Habilidade(1, h));
				}else {
					for(Habilidade hab: lista) {
						if(hab.getNome().equalsIgnoreCase(h)) {
							int qtd = hab.getQuantidade();
							hab.setQuantidade(qtd + 1);
							found = true;
							break;
						}
					}
					if(!found) {
						lista.add(new Habilidade(1, h));
					}
				}
			}
		}
		Collections.sort(lista);
		Collections.reverse(lista);
		List<Habilidade> topTres = new ArrayList<>();
		int count = 0;
		for(Habilidade h: lista) {
			if(count < 3) {
				topTres.add(h);
			}
			count++;
		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(topTres.stream().map(e -> mapper.map(e, Habilidade.class)).collect(Collectors.toList()));
	}
	
	@GetMapping("/pokemons/tipos/toptres")
	public ResponseEntity<List<Tipo>> topTresTipos() {
		List<Pokemon> pokemons = repo.findAll();
		List<Tipo> lista = new ArrayList<>();
		for (Pokemon p : pokemons){
				boolean found = false;
				if(lista.isEmpty()) {
					lista.add(new Tipo(1, p.getTipo()));
				}else{
					for(Tipo tipo: lista) {
						if(tipo.getNome().equalsIgnoreCase(p.getTipo())) {
							int qtd = tipo.getQuantidade();
							tipo.setQuantidade(qtd + 1);
							found = true;
							break;
						}
					}
					if(!found) {
						lista.add(new Tipo(1, p.getTipo()));
					}
				
			}
		}
		Collections.sort(lista);
		Collections.reverse(lista);
		List<Tipo> topTres = new ArrayList<>();
		int count = 0;
		for(Tipo t: lista) {
			if(count < 3) {
				topTres.add(t);
			}
			count++;
		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(topTres.stream().map(e -> mapper.map(e, Tipo.class)).collect(Collectors.toList()));
	}

}
