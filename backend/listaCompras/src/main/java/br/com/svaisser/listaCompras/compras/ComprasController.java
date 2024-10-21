package br.com.svaisser.listaCompras.compras;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.svaisser.listaCompras.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
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
            message.append("Os seguintes itens já estavam na lista e não foram adicionados: ")
                    .append(String.join(", ", duplicados)).append(". ");
        }

        if (!adicionados.isEmpty()) {
            message.append("Os seguintes itens foram adicionados com sucesso: ")
                    .append(String.join(", ", adicionados)).append(".");
        }

        if (adicionados.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message.toString());
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(message.toString());
        }

    }

    @SuppressWarnings({ "rawtypes", "unused" })
    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody ComprasModel comprasModel, @PathVariable Integer id,
            HttpServletRequest request) {
        var compras = this.comprasRepository.findById(id);
        System.out.println(compras);

        if (compras == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Compra não encontrada.");
        } else {
            Utils.copyNonNullProperties(comprasModel, compras);
            var result = this.comprasRepository.save(compras);

            return ResponseEntity.ok().body("Compra Editada com sucesso! " + result);
        }
    }
}