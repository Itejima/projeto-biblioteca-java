# Trabalho Final - Laboratório de Banco de Dados: Sistema de Biblioteca

Este é o projeto final para a disciplina de Laboratório de Banco de Dados. O objetivo foi construir uma aplicação full-stack completa que demonstra o domínio de conceitos avançados de SGBDs.

A aplicação é um Sistema de Gerenciamento de Biblioteca que utiliza uma arquitetura de **persistência poliglota**, empregando um banco de dados relacional (MySQL) para dados transacionais estruturados e um banco de dados NoSQL (MongoDB) para dados semi-estruturados (reviews de livros).

---

##  Tecnologias Utilizadas

* **Backend:** Java 17, Spring Boot, Spring Data JPA, Spring Data MongoDB, Spring Security (para senhas)
* **Banco de Dados (Híbrido):**
    * **SQL:** MySQL 8
    * **NoSQL:** MongoDB
* **Frontend:** HTML5, CSS3, JavaScript (Fetch API)
* **Ferramentas:** Maven, Postman, MySQL Workbench, MongoDB Compass

---

##  Arquitetura

A aplicação segue um padrão de API REST:

**Frontend (Cliente)** (`index.html`) $\rightarrow$ **Backend (API REST)** (`Spring Boot`) $\rightarrow$ **Bancos de Dados**

O Backend (Spring Boot) é responsável por orquestrar os dois bancos de dados:
* **MySQL:** Armazena todos os dados estruturados e transacionais (usuários, livros, autores, empréstimos).
* **MongoDB:** Armazena os dados semi-estruturados (reviews e comentários dos livros).

---

##  Demonstração dos Requisitos Obrigatórios

Esta seção detalha como cada requisito obrigatório do trabalho foi implementado.

### 1. Banco de Dados Relacional (MySQL)

O `script.sql` (incluso neste repositório) contém a implementação completa:

* **Controle de Acesso (Usuários):** Foram criados dois usuários (`app_biblioteca` e `app_leitura`). A aplicação **não** se conecta como `root`. O usuário `app_biblioteca` (usado pelo Spring) tem permissões de CRUD e `EXECUTE` para as procedures/functions, enquanto o `app_leitura` (para relatórios) tem acesso `SELECT` apenas a *Views* específicas, garantindo a segurança.
* **Geração de IDs Customizados:** Para dados críticos (usuários e empréstimos), o `AUTO_INCREMENT` foi evitado.
    * `fnc_gerar_id_usuario()`: Gera um ID (`USR` + Data + Aleatório) para novos usuários.
    * `fnc_gerar_id_emprestimo()`: Gera um ID (`EMP` + Data + Sequencial) para novas transações.
* **Índices (2):**
    * `idx_livro_titulo`: Otimiza a funcionalidade mais comum do sistema (busca por título).
    * `idx_emprestimo_usuario_ativo`: Otimiza a consulta de "meus empréstimos" e a verificação de pendências.
* **Views (2):**
    * `vw_livros_disponiveis_catalogo`: Abstrai a complexidade dos `JOINs` (livro, autor, categoria) e serve como camada de segurança (mostra apenas livros com estoque > 0) para o painel de Membros.
    * `vw_emprestimos_atrasados`: Um relatório de segurança focado para o Bibliotecário.
* **Triggers (2):**
    * `trg_atualizar_estoque_emprestimo`: Disparado `AFTER INSERT` em `emprestimos`, garante a integridade dos dados de inventário decrementando o estoque do livro.
    * `trg_impedir_exclusao_autor_com_livros`: Disparado `BEFORE DELETE` em `autores`, reforça a integridade referencial ao impedir que um autor com livros cadastrados seja excluído.
* **Procedure (1) e Functions (3):**
    * `sp_registrar_devolucao`: Uma Stored Procedure transacional que garante a **atomicidade** da devolução (atualiza o empréstimo e incrementa o estoque do livro).
    * `fnc_verificar_pendencias_usuario`: Função que centraliza uma regra de negócio complexa (se o usuário pode ou não alugar) e é reutilizada pelo `EmprestimoService`.

### 2. Banco de Dados NoSQL (MongoDB)

* **Justificativa da Escolha:** O MongoDB (um banco de Documentos) foi escolhido para armazenar os **reviews e comentários** dos livros.
* **Aplicação:** Reviews são dados semi-estruturados (nota, texto, data, ID do usuário). Armazená-los como documentos JSON no MongoDB é mais flexível e escalável do que em tabelas SQL rígidas. O backend implementa uma arquitetura de persistência poliglota, onde o `ReviewService` consulta o MySQL para obter o nome do usuário e, em seguida, salva o documento de review no MongoDB, demonstrando a integração dos dois SGBDs.

---

##  Como Executar o Projeto

#### 1. Pré-requisitos
* Java 17+ (JDK)
* Apache Maven
* MySQL Server 8
* MySQL Workbench
* MongoDB Community Server (instalado como serviço)
* MongoDB Compass
* Postman

#### 2. Configuração do Banco de Dados (MySQL)
1.  Abra o **MySQL Workbench**.
2.  Abra e execute o arquivo `script.sql` (incluso neste repositório) para criar o banco `biblioteca_db`, todas as tabelas, views, procedures, e inserir os dados de teste.

#### 3. Configuração do Banco de Dados (MongoDB)
1.  Abra o **MongoDB Compass**.
2.  Conecte-se ao servidor local (`mongodb://localhost:27017`).
3.  O banco `biblioteca_reviews` será criado automaticamente pelo Spring Boot quando o primeiro review for salvo.

#### 4. Executando o Backend (Java)
1.  Abra o projeto na sua IDE (IntelliJ ou VSCode).
2.  Deixe o Maven baixar todas as dependências do `pom.xml`.
3.  Localize e execute o arquivo `src/main/java/com/biblioteca/biblioteca/BibliotecaApplication.java`.
4.  Aguarde o console mostrar `Tomcat started on port(s): 8080...`.

#### 5. Executando o Frontend (Cliente)
1.  Localize o arquivo `index.html` (incluso neste repositório).
2.  Abra-o em qualquer navegador (Chrome, Firefox).

---

##  Como Testar a Aplicação

A aplicação possui dois níveis de acesso: Membro (Grupo 3) e Bibliotecário (Grupo 2).

### 1. Testando como Membro
1.  No `index.html`, clique em "Ir para Registro".
2.  Registre um novo usuário (ex: `membro@email.com`, senha `123`). O formulário registra automaticamente como **Grupo 3 (Membro)**.
3.  Faça login com o usuário que você acabou de criar.
4.  Você verá o **Painel do Membro**.
5.  Clique em "Carregar Catálogo". Os livros de teste (Duna, O Hobbit, etc.) aparecerão.
6.  Clique em "Emprestar" em um dos livros. A requisição será enviada e o estoque será atualizado (via Trigger).

### 2. Testando como Bibliotecário
O registro pelo frontend é bloqueado para Membros. Para criar um Bibliotecário, é preciso usar o Postman para forçar o `idGrupo: 2`.

1.  Abra o **Postman**.
2.  Crie uma requisição `POST` para `http://localhost:8080/api/registrar`.
3.  Vá em **Body** $\rightarrow$ **raw** $\rightarrow$ **JSON** e cole o seguinte:
    ```json
    {
      "nome": "Bibliotecário Chefe",
      "email": "biblio@email.com",
      "senha": "admin123",
      "idGrupo": 2
    }
    ```
4.  Clique em **Send**.
5.  Volte ao `index.html` e faça login com `biblio@email.com` e senha `admin123`.
6.  Você verá o **Painel de Administração**, onde pode adicionar e deletar livros. As ações refletirão no catálogo dos membros.

### 3. Testando o NoSQL (MongoDB)
1.  No **Postman**, crie uma requisição `POST` para `http://localhost:8080/api/reviews`.
2.  Vá em **Body** $\rightarrow$ **raw** $\rightarrow$ **JSON** e cole (use um ID de usuário e livro válidos do seu banco SQL):
    ```json
    {
      "idLivro": 1,
      "idUsuario": "USR...", 
      "nota": 5,
      "comentario": "Este livro é incrível! Testando o NoSQL."
    }
    ```
3.  Clique em **Send**.
4.  Abra o **MongoDB Compass**. Atualize a lista de bancos. Você verá o `biblioteca_reviews` e, dentro dele, a coleção `reviews` com o documento que você acabou de salvar.

