package br.com.svaisser.listaCompras.compras;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.svaisser.listaCompras.users.IUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PutMapping;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/compras")
public class ComprasController {

  @Autowired
  private IComprasRepository IComprasRepository;

  @Autowired
  private IUserRepository IUserRepository;

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/{idUser}")
  public ResponseEntity<?> getCompras(HttpServletRequest request, @PathVariable Integer idUser,
      Authentication authentication) {
    System.out.println("Teste Controller GetCompras idUser: " + idUser);

    Integer userId = (Integer) authentication.getPrincipal();
    if (!userId.equals(idUser)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    List<ComprasModel> comprasList = this.IComprasRepository.findByIdUser(idUser);
    return ResponseEntity.ok(comprasList);
  }

  @PostMapping("/create")
  public ResponseEntity<?> create(@RequestBody List<ComprasModel> comprasModelList, Authentication authentication) {
    List<String> adicionados = new ArrayList<>();

    for (ComprasModel comprasModel : comprasModelList) {
      Integer idUser = comprasModel.getIdUser();

      if (idUser == null || !IUserRepository.existsById(idUser)) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Usuário não autorizado ou inexistente");
      }

      List<ComprasModel> comprasList = this.IComprasRepository.findByIdUser(idUser);

      if (comprasList.isEmpty()) {
        this.IComprasRepository.save(comprasModel);
        adicionados.add(comprasModel.getItem());
        continue;
      }

      for (ComprasModel comprasUser : comprasList) {
        comprasModel.setIdUser(idUser);
        var comprasExistentes = this.IComprasRepository.findByItem(comprasModel.getItem());

        if (comprasExistentes != null && !comprasExistentes.isEmpty()) {
          if (comprasExistentes.getIdUser() == comprasUser.getIdUser()) {
            continue; 
          }
        }

        this.IComprasRepository.save(comprasModel);
        adicionados.add(comprasModel.getItem());
      }
    }

    if (adicionados.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nenhum item novo adicionado.");
    } else {
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(Map.of("mensagem", "Itens adicionados: " + String.join(", ", adicionados)));
    }
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<?> updateCompra(HttpServletRequest request, @PathVariable Integer id,
      @RequestBody ComprasModel compraDetails) {

    Optional<ComprasModel> compraOptional = IComprasRepository.findById(id);
    if (compraOptional.isPresent()) {
      ComprasModel compra = compraOptional.get();
      compra.setItem(compraDetails.getItem());
      compra.setQuantia(compraDetails.getQuantia());
      compra.setDescricao(compraDetails.getDescricao());
      ComprasModel updatedCompra = IComprasRepository.save(compra);
      System.out.println(updatedCompra);
      return ResponseEntity.ok(updatedCompra);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @SuppressWarnings("unused")
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> delete(HttpServletRequest request, @PathVariable Integer id) {

    Optional<ComprasModel> compra = IComprasRepository.findById(id);
    if (compra != null) {
      IComprasRepository.deleteById(id);
      return ResponseEntity.ok().body(Map.of("message", "Item excluído com sucesso", "id", id));
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Item não encontrado", "id", id));
    }
  }
}
