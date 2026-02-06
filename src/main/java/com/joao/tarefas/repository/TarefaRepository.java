package com.joao.tarefas.repository;

import com.joao.tarefas.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    boolean existsByNome(String nome);

    Optional<Tarefa> findByNome(String nome);

    List<Tarefa> findAllByOrderByOrdemAsc();

    Optional<Tarefa> findByOrdem(Integer ordem);
}
