# 🛒 Lista de Compras 2.0 🛒

## Status do Projeto

🚧 Projeto em desenvolvimento. As principais funcionalidades ainda estão sendo implementadas.

## Índice

- [Descrição](#descrição)
- [Tecnologias](#tecnologias)
- [Funcionalidades](#funcionalidades)
- [Roadmap](#roadmap)
- [Rodando o Projeto](#rodando-o-projeto)

## Descrição

A Lista de Compras 2.0 é uma aplicação web para gerenciar uma lista de compras, utilizando uma arquitetura full stack. Esta versão oferece persistência de dados, integração de API, e um design otimizado para facilitar o gerenciamento das compras.

## Tecnologias

- **Back-end:** Java, Spring Boot
- **Banco de Dados:** H2 (em memória)
- **Front-end:** JavaScript, AngularJS
- **Gerenciamento de dependências:** Maven, npm

## Funcionalidades

- [X] Adicionar, editar e remover itens da lista de compras
- [X] Banco de dados H2 em memória
- [X] Sincronização em tempo real entre front-end e back-end
- [X] Suporte para banco de dados persistente o PostgreSQL
- [X] Implementar autenticação de usuário

## Roadmap

- [ ] Criar interface para exportar a lista de compras como PDF


## Rodando o projeto

### Back-end

1. **Inicie o servidor PostgreSQL**:
   ```bash
   sudo systemctl start postgresql 
   # Para verificar o serviço
   sudo systemctl status postgresql
2. **Execute o arquivo**:
    
    [ListaComprasApplication.java](backend/listaCompras/src/main/java/br/com/svaisser/listaCompras/ListaComprasApplication.java)

3. **Rodando o Front**:
   ```bash
   cd frontend
   npm install
   npm start
### Observações Finais

O projeto foi desenvolvido para funcionar com PostgreSQL. Certifique-se de que o PostgreSQL está em execução sempre que for iniciar a aplicação. Além de que no seu postgres é necessário configurar a senha padrão. O padrão do projeto é senha: admin, caso precise alterar -> [application.properties](backend/listaCompras/src/main/resources/application.properties)

**Opcional:** O projeto pode ser executado com um banco de dados H2 para testes. Para isso, as configurações do H2 estão disponíveis em [application.properties](backend/listaCompras/src/main/resources/application.properties) e podem ser acessadas através do console H2 em http://localhost:8080/h2-console/ com Login: admin e Password: admin.

- Isso seria para usar PostgreSQL, mas também oferecem uma opção para testes com H2, caso desejem.

