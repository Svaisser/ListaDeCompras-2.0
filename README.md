# Lista de Compras 2.0

Essa é uma versão mais completa da lista de compras anterior a qual tinha apenas o front end, já nesta nova versão dividi o projeto em 2 pastas/portas onde uma estará rodando o front e a outra o back com acesso a um banco de dados em memória H2.

## Instalação

Siga as etapas abaixo no terminal para rodar o projeto (NO LINUX):


### Instalar o JDK 17

Caso vá executar no VSCode será necessário instalar também as extenções do Java e Spring

sudo apt update
sudo apt install openjdk-17-jdk
java -version

#### Caso precise mude a versão do JDK
sudo update-alternatives --config java

### Instalar o Maven

wget https://archive.apache.org/dist/maven/maven-3/3.5.4/binaries/apache-maven-3.5.4-bin.tar.gz
tar -xvzf apache-maven-3.5.4-bin.tar.gz
sudo mv apache-maven-3.5.4 /opt/
nano ~/.bashrc

#### Adicione as seguintes linhas no final do arquivo

export M2_HOME=/opt/apache-maven-3.5.4
export MAVEN_HOME=/opt/apache-maven-3.5.4
export PATH=${M2_HOME}/bin:${PATH}

source ~/.bashrc
mvn -version

### Instalar o Node

curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
sudo apt install -y nodejs
npm -v

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
