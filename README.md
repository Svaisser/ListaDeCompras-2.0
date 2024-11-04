# 🛒 Lista de Compras 2.0 🛒

## Status do Projeto

🚧 Projeto em desenvolvimento. As principais funcionalidades foram concluídas em 04/11/2024, mas ainda há ideias a serem implementadas.

## Índice

- [Descrição](#descrição)
- [Tecnologias](#tecnologias)
- [Funcionalidades](#funcionalidades)
- [Roadmap](#roadmap)
- [Executando o Projeto](#executando-o-projeto)
- [Contato](#contato)

## Descrição

A **Lista de Compras 2.0** é uma aplicação web para gerenciar uma lista de compras, utilizando uma arquitetura full stack. Esta versão oferece persistência de dados, integração com API e um design otimizado para facilitar o gerenciamento das compras.

## Tecnologias

- **Back-end:** Java, Spring Boot
- **Banco de Dados:** H2 (em memória) e PostgreSQL (persistente)
- **Front-end:** JavaScript, AngularJS
- **Gerenciamento de dependências:** Maven, npm

## Funcionalidades

- [X] Adicionar, editar e remover itens da lista de compras
- [X] Banco de dados H2 em memória para testes
- [X] Sincronização em tempo real entre front-end e back-end
- [X] Suporte para banco de dados persistente com PostgreSQL
- [X] Implementação de autenticação de usuário

## Roadmap

- [ ] Criar interface para exportar a lista de compras como PDF

## Executando o Projeto

### Back-end

1. **Inicie o servidor PostgreSQL**:
   ```bash
   sudo systemctl start postgresql 
   # Para verificar o status do serviço
   sudo systemctl status postgresql
   ```
   
2. **Execute a aplicação**:
    Execute o arquivo [ListaComprasApplication.java](backend/listaCompras/src/main/java/br/com/svaisser/listaCompras/ListaComprasApplication.java).

### Front-end

1. **Rodando o Front-end**:
   ```bash
   cd frontend
   npm install
   npm start
   ```

### Observações Finais

As versões usadas são:

- Java 17
- Maven 3.6.3
- npm 10.8.3

O projeto foi desenvolvido para funcionar com PostgreSQL. Certifique-se de que o PostgreSQL está em execução antes de iniciar a aplicação. A senha padrão para o banco de dados no projeto é `admin`. Caso precise alterá-la, ajuste no arquivo [application.properties](backend/listaCompras/src/main/resources/application.properties).

**Opcional:** Para testes, é possível rodar o projeto com o banco de dados H2. As configurações do H2 estão disponíveis no mesmo arquivo [application.properties](backend/listaCompras/src/main/resources/application.properties), e o console H2 pode ser acessado em http://localhost:8080/h2-console/ com as credenciais `Login: admin` e `Password: admin`.

## Contato

Em caso de dúvidas ou sugestões, entre em contato:
- **Email:** svaisserjv@gmail.com
  