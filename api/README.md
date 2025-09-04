# **GCP \- Gestor de Comprovantes de Pagamentos (API Backend)**

Bem-vindo ao backend do projeto GCP \- Gestor de Comprovantes de Pagamentos. Esta API, desenvolvida com Java e Spring Boot, é o coração do sistema, responsável por toda a lógica de negócio, incluindo o gerenciamento de fornecedores e o processamento e envio de notas fiscais.

## **🚀 Tecnologias Utilizadas**

Este projeto foi construído utilizando um stack de tecnologias modernas e robustas, focadas em qualidade, manutenibilidade e escalabilidade.

* **Linguagem:** Java 17  
* **Framework:** Spring Boot 3+  
* **Build Tool:** Gradle com Kotlin DSL  
* **Banco de Dados:** PostgreSQL  
* **Migrations:** Flyway  
* **Arquitetura:** Arquitetura Hexagonal (Portas e Adaptadores)  
* **Documentação da API:** OpenAPI 3 (via Springdoc)  
* **Conteinerização:** Docker & Docker Compose  
* **Testes:**  
  * **Unidade:** JUnit 5 & Mockito  
  * **Integração:** Testcontainers (para PostgreSQL)  
  * **Assertivas:** AssertJ

## **🏗️ Arquitetura**

A API segue os princípios da **Arquitetura Hexagonal (Portas e Adaptadores)** para garantir um forte desacoplamento entre a lógica de negócio e os detalhes de infraestrutura (web, persistência, etc.).

* **domain**: O núcleo da aplicação, contendo os modelos de negócio puros.  
* **application**: Contém os casos de uso (Use Cases) e as interfaces (Ports) que definem os contratos da aplicação.  
* **infrastructure**: Implementações concretas (Adapters) que interagem com o mundo externo (Controllers REST, repositórios JPA, envio de e-mails, etc.).

## **🛠️ Como Executar a Aplicação (com Docker)**

A forma mais simples e recomendada de executar a aplicação completa (API \+ Banco de Dados) é utilizando o Docker Compose.

### **Pré-requisitos**

* [Docker](https://www.docker.com/products/docker-desktop/) instalado e em execução.

### **Passos para Execução**

1. **Crie o Arquivo de Ambiente (.env)**  
   Na raiz do projeto (GCP/), crie um arquivo chamado .env. Este arquivo guardará as suas credenciais e configurações locais e **não deve ser enviado para o repositório Git**.  
   Copie e cole o conteúdo abaixo no seu .env e preencha com as suas credenciais:  
   \# Arquivo de Variáveis de Ambiente para Docker Compose  
   \# PREENCHA COM AS SUAS CREDENCIAIS.

   \# \--- Configurações do Banco de Dados PostgreSQL \---  
   POSTGRES\_DB=gcp\_db  
   POSTGRES\_USER=gcp\_user  
   POSTGRES\_PASSWORD=gcp\_password

   \# \--- Configurações do Servidor de E-mail (Exemplo com Gmail) \---  
   SPRING\_MAIL\_HOST=smtp.gmail.com  
   SPRING\_MAIL\_PORT=587  
   SPRING\_MAIL\_USERNAME=seu-email@gmail.com  
   SPRING\_MAIL\_PASSWORD=sua-senha-de-app-aqui

2. **Inicie os Contêineres**  
   Abra um terminal na raiz do projeto (GCP/) e execute o seguinte comando:  
   docker-compose up \--build

   O Docker irá construir a imagem da API, baixar a imagem do PostgreSQL e iniciar ambos os serviços.  
3. **Acesse a Aplicação**  
   * A API estará disponível em http://localhost:8080.

## **📖 Documentação da API (Swagger)**

Com a aplicação em execução, você pode aceder a uma documentação interativa da API, gerada automaticamente pelo OpenAPI (Swagger).

* **URL do Swagger UI:** [http://localhost:8080/swagger-ui.html](https://www.google.com/search?q=http://localhost:8080/swagger-ui.html)

Nesta página, você pode visualizar todos os endpoints, os seus DTOs e até mesmo executar requisições de teste diretamente do navegador.

## **✅ Como Executar os Testes**

O projeto possui uma suíte completa de testes de unidade e integração.

### **Pré-requisitos**

* JDK 17+ instalado.  
* Docker instalado e em execução (para os testes de integração com Testcontainers).

### **Configuração para Testes Locais**

Para executar os testes de integração, que dependem de um CNPJ válido, precisamos de configurar o ambiente de build do Gradle.

1. **Crie o Arquivo gradle.properties**  
   Dentro da pasta da API (GCP/api/), crie um arquivo chamado gradle.properties. Este arquivo **não deve ser enviado para o repositório Git**.  
   Adicione o seu CNPJ válido a este arquivo:  
   \# Define o CNPJ a ser usado nos testes de integração.  
   TEST\_CNPJ=\<SEU\_CNPJ\_VALIDO\_AQUI\>

2. **Execute os Testes**  
   Abra um terminal na raiz da API (GCP/api/) e execute o comando:  
   ./gradlew test

   O Gradle irá compilar o código e executar todos os testes de unidade e integração. Os testes de integração irão iniciar um contêiner Docker temporário para o banco de dados, garantindo um ambiente de teste limpo e fiável.