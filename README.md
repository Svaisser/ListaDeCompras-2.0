# Lista de Compras 2.0

Essa é uma versão mais completa da lista de compras anterior a qual tinha apenas o front end, já nesta nova versão dividi o projeto em 2 pastas/portas onde uma estará rodando o front e a outra o back com acesso a um banco de dados em memória H2.

## Rodando o projeto

### Back-end

Siga o caminho abaixo e execute a aplicação.
backend/listaCompras/src/main/java/br/com/svaisser/listaCompras/ListaComprasApplication.java

#### Banco de dados Teste

http://localhost:8080/h2-console/
Login: admin
Password: admin
O H2 é configurado em backend/listaCompras/src/main/resources/application.properties

### Front-end

cd frontend
npm install
npm start
