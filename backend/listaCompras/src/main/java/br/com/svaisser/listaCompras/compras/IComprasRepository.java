package br.com.svaisser.listaCompras.compras;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IComprasRepository extends JpaRepository<ComprasModel, Integer>{

  ComprasModel findByItem(String item);

}