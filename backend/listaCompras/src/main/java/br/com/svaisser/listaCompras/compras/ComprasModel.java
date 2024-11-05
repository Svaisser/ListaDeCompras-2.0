package br.com.svaisser.listaCompras.compras;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table
@Entity(name = "tb_compras")

public class ComprasModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer idUser;

    private String item;
    private Double quantia;
    private String descricao;
    private String categoria;

    public boolean isEmpty() {
        return (id == null || id <= 0) && (idUser == null || idUser <= 0)
                && (item == null || item.isEmpty())
                && (quantia == null || quantia <= 0);
    }
}