package br.com.svaisser.listaCompras;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "br.com.svaisser.listaCompras")
public class ListaComprasApplication {

	public static void main(String[] args) {
		SpringApplication.run(ListaComprasApplication.class, args);
	}

}
