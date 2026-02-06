package com.joao.tarefas.service;

import com.joao.tarefas.model.Tarefa;
import com.joao.tarefas.repository.TarefaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TarefaService {

    private final TarefaRepository repository;

    public TarefaService(TarefaRepository repository) {
        this.repository = repository;
    }


    public List<Tarefa> listar() {
        return repository.findAllByOrderByOrdemAsc();
    }


    public Tarefa criar(Tarefa tarefa) {

        int novaOrdem = repository.findAllByOrderByOrdemAsc().size() + 1;

        tarefa.setOrdem(novaOrdem);

        return repository.save(tarefa);
    }


    public Tarefa atualizar(Long id, Tarefa nova) {

        Tarefa atual = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        atual.setNome(nova.getNome());
        atual.setCusto(nova.getCusto());
        atual.setDataLimite(nova.getDataLimite());

        return repository.save(atual);
    }


    @Transactional
    public void excluir(Long id) {
        Tarefa removida = repository.findById(id)
                .orElseThrow();

        int ordem = removida.getOrdem();

        repository.delete(removida);

        List<Tarefa> tarefas = repository.findAllByOrderByOrdemAsc();

        for (Tarefa t : tarefas) {
            if (t.getOrdem() > ordem) {
                t.setOrdem(t.getOrdem() - 1);
                repository.save(t);
            }
        }
    }


    @Transactional
    public void subir(Long id) {

        Tarefa atual = repository.findById(id)
                .orElseThrow();

        if (atual.getOrdem() == null || atual.getOrdem() <= 1) return;

        Tarefa anterior = repository
                .findByOrdem(atual.getOrdem() - 1)
                .orElse(null);

        if (anterior == null) return;

        trocarOrdem(atual, anterior);
    }


    @Transactional
    public void descer(Long id) {

        Tarefa atual = repository.findById(id)
                .orElseThrow();

        if (atual.getOrdem() == null) return;

        Tarefa proxima = repository
                .findByOrdem(atual.getOrdem() + 1)
                .orElse(null);

        if (proxima == null) return;

        trocarOrdem(atual, proxima);
    }


    @Transactional
    private void trocarOrdem(Tarefa a, Tarefa b) {

        int ordemA = a.getOrdem();
        int ordemB = b.getOrdem();

        a.setOrdem(-1000);
        repository.saveAndFlush(a);

        b.setOrdem(ordemA);
        repository.saveAndFlush(b);


        a.setOrdem(ordemB);
        repository.saveAndFlush(a);
    }

}
