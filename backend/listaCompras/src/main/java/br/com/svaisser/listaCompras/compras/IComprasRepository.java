package br.com.svaisser.listaCompras.compras;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface IComprasRepository extends JpaRepository<ComprasModel, Integer>{

  ComprasModel findByItem(String item);

  @Transactional
  void deleteByItem(String item);

  @Override
  @SuppressWarnings({ "null", "unchecked" })
  ComprasModel save(ComprasModel compra);

}