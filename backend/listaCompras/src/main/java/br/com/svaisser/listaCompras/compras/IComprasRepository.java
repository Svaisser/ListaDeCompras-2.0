package br.com.svaisser.listaCompras.compras;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface IComprasRepository extends JpaRepository<ComprasModel, Integer>{

  ComprasModel findByItem(String item);

  List<ComprasModel> findByIdUser(Integer idUser);

  // ComprasModel findByIdAndByIdUser(Integer id, Integer idUser);

  @Transactional
  void deleteByItem(String item);

  @Override
  @SuppressWarnings({ "null", "unchecked" })
  ComprasModel save(ComprasModel compra);

}