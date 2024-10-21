package br.com.svaisser.listaCompras.compras;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/compras")

public class ComprasController {

    @Autowired
    private IComprasRepository userRepository;

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody List<ComprasModel> comprasModelList) {

        List<String> duplicados = new ArrayList<String>();
        List<String> adicionados = new ArrayList<String>();

        for (ComprasModel comprasModel : comprasModelList) {
            var user = this.userRepository.findByItem(comprasModel.getItem());

            if (user != null) {
                duplicados.add(comprasModel.getItem()); 
            } else {
                this.userRepository.save(comprasModel); 
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
}