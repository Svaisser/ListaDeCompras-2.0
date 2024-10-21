package br.com.svaisser.listaCompras.compras;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tb_compras")

public class ComprasModel {

    @Id
    private int id;

    @Column(unique = true)
    private String item;
    private Double quantia;
    private String descricao;

    @CreationTimestamp
    private LocalDateTime createdAt;
}