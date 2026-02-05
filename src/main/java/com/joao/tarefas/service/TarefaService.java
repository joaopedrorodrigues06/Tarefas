package com.joao.tarefas.service;

import com.joao.tarefas.model.Tarefa;
import com.joao.tarefas.repository.TarefaRepository;
import org.springframework.stereotype.Service;

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

        Integer ultimaOrdem = repository.findAllByOrderByOrdemAsc()
                .stream()
                .map(Tarefa::getOrdem)
                .max(Integer::compareTo)
                .orElse(0);

        tarefa.setOrdem(ultimaOrdem + 1);

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

    public void excluir(Long id) {
        repository.deleteById(id);
    }

    public void subir(Long id) {
        mover(id, -1);
    }

    public void descer(Long id) {
        mover(id, 1);
    }

    private void mover(Long id, int direcao) {
        List<Tarefa> tarefas = repository.findAllByOrderByOrdemAsc();

        int index = -1;
        for (int i = 0; i < tarefas.size(); i++) {
            if (tarefas.get(i).getId().equals(id)) {
                index = i;
                break;
            }
        }

        int novoIndex = index + direcao;
        if (index < 0 || novoIndex < 0 || novoIndex >= tarefas.size()) return;

        Tarefa atual = tarefas.get(index);
        Tarefa outra = tarefas.get(novoIndex);

        int temp = atual.getOrdem();
        atual.setOrdem(outra.getOrdem());
        outra.setOrdem(temp);

        repository.save(atual);
        repository.save(outra);
    }
}
