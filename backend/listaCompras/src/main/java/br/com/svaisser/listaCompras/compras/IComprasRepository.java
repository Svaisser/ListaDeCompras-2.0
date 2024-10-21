package br.com.svaisser.listaCompras.compras;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IComprasRepository extends JpaRepository<ComprasModel, Integer>{

  ComprasModel findByItem(String item);

  Object save(Optional<ComprasModel> compra);

}