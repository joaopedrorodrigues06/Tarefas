package com.joao.tarefas.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "tarefas", uniqueConstraints = {
        @UniqueConstraint(columnNames = "nome"),
        @UniqueConstraint(columnNames = "ordem")
})
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private BigDecimal custo;

    @NotNull
    @Column(name = "data_limite", nullable = false)
    private LocalDate dataLimite;


    @Column(nullable = false)
    private Integer ordem;



}