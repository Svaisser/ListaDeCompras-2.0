package br.com.svaisser.listaCompras.compras;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.var;

import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/compras")

public class ComprasController {

    @Autowired
    private IComprasRepository comprasRepository;

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody List<ComprasModel> comprasModelList) {

        List<String> duplicados = new ArrayList<String>();
        List<String> adicionados = new ArrayList<String>();

        for (ComprasModel comprasModel : comprasModelList) {
            var compras = this.comprasRepository.findByItem(comprasModel.getItem());

            if (compras != null) {
                duplicados.add(comprasModel.getItem());
            } else {
                this.comprasRepository.save(comprasModel);
                adicionados.add(comprasModel.getItem());
            }
        }

        StringBuilder message = new StringBuilder();
        if (!duplicados.isEmpty()) {
            message.append("Os itens já estavam, logo não foram add: \n")
                    .append(String.join(", ", duplicados)).append(". \n");
        }

        if (!adicionados.isEmpty()) {
            message.append("Os seguintes itens foram adicionados com sucesso: \n")
                    .append(String.join(", ", adicionados)).append(".\n");
        }

        if (adicionados.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message.toString());
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(message.toString());
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<ComprasModel> updateCompra(@PathVariable Integer id,
            @RequestBody ComprasModel compraDetails) {
        Optional<ComprasModel> compraOptional = comprasRepository.findById(id);

        if (compraOptional.isPresent()) {
            ComprasModel compra = compraOptional.get(); // Extraímos o valor do Optional

            // Atualize os campos que você deseja
            compra.setItem(compraDetails.getItem());
            compra.setQuantia(compraDetails.getQuantia());
            compra.setDescricao(compraDetails.getDescricao());

            // Salve a entidade atualizada
            ComprasModel updatedCompra = comprasRepository.save(compra);

            return ResponseEntity.ok(updatedCompra);
        } else {
            return ResponseEntity.notFound().build(); // Se o ID não for encontrado
        }
    }

    
    @DeleteMapping("/delete/{item}")
    public ResponseEntity<Void> delete(@PathVariable String item) {

        ComprasModel compra = comprasRepository.findByItem(item);

        if (compra != null) {
            comprasRepository.deleteByItem(item);
            System.out.println("Item excluído: " + item);
            return ResponseEntity.ok().build(); // Retorna status 200 OK após exclusão
        } else {
            System.out.println("Item não encontrado: " + item);
            return ResponseEntity.notFound().build(); // Retorna status 404 Not Found
        }

    }

}