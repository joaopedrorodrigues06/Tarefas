package com.joao.tarefas.controller;

import com.joao.tarefas.model.Tarefa;
import com.joao.tarefas.service.TarefaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
@CrossOrigin
public class TarefaController {

    private final TarefaService service;

    public TarefaController(TarefaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Tarefa> listar() {
        return service.listar();
    }

    @PostMapping
    public Tarefa criar(@RequestBody @Valid Tarefa tarefa) {
        return service.criar(tarefa);
    }

    @PutMapping("/{id}")
    public Tarefa atualizar(@PathVariable Long id,
                            @RequestBody @Valid Tarefa tarefa) {
        return service.atualizar(id, tarefa);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }

    @PutMapping("/{id}/subir")
    public void subir(@PathVariable Long id) {
        service.subir(id);
    }

    @PutMapping("/{id}/descer")
    public void descer(@PathVariable Long id) {
        service.descer(id);
    }
}


