package br.ufpr.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufpr.model.DeletedHistory;

public interface DeletedHistoryRepository extends JpaRepository<DeletedHistory, Long> {

	Optional<DeletedHistory> findByNome(String nome);

}