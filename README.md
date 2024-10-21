# Lista de Compras 2.0

<p align="center">
  <img alt="Página da lista de compras" src=".github/frontend.jpg" width="75%" style="border-radius:32px;">
</p>

## Índice
- [Descrição](#descrição)
- [Tecnologias](#tecnologias)
- [Rodando o Projeto](#rodando-o-projeto)

## Descrição
A Lista de Compras 2.0 é uma aplicação web para gerenciar uma lista de compras, utilizando uma arquitetura full stack. Esta versão oferece persistência de dados, integração de API, e um design otimizado para facilitar o gerenciamento das compras.


## Tecnologias
- **Back-end:** Java, Spring Boot
- **Banco de Dados:** H2 (em memória)
- **Front-end:** JavaScript, AngularJS
- **Gerenciamento de dependências:** Maven, npm


## Rodando o projeto

### Back-end

Siga o caminho abaixo e execute a aplicação. <br>
backend/listaCompras/src/main/java/br/com/svaisser/listaCompras/ListaComprasApplication.java

#### Banco de dados Teste

http://localhost:8080/h2-console/ <br>
Login: admin <br>
Password: admin <br>
O H2 é configurado em backend/listaCompras/src/main/resources/application.properties <br>

### Front-end

cd frontend<br>
npm install<br>
npm start<br>
