# Desafio - Backend JAVA  

## 📌 Instruções
- Desenvolver as novas funcionalidades de acordo com as instruções fornecidas pelo RH no arquivo PDF.
- Respeitar os padrões de arquitetura, nomenclatura e frameworks definidos na nossa stack.

---

## 🏆 Avaliação de Desenvolvedor de Software

### **Roteiro - Backend (Java)**

Dê continuidade ao projeto backend escolhendo um dos desafios abaixo, de acordo com seu nível de conhecimento.  
**Observe que o avanço de nível sempre inclui os requisitos do nível anterior.**  

O projeto consiste em uma **API para e-commerce**, e a listagem de produtos já está criada.

### 📂 **Desafios**
#### 🟢 **Iniciante**
✅ Finalizar o Cadastro de produtos com os demais métodos para criação de um CRUD.

#### 🟡 **Intermediário**
✅ Requisitos da fase iniciante + criar uma API para categorias e relacionar uma categoria ao produto, permitindo:
- A recuperação de produtos por categoria.
- A inclusão e alteração da categoria de um produto.

#### 🔵 **Avançado**
✅ Requisitos da fase intermediário + implementar **autenticação JWT** com **RBAC**.

#### 🔴 **Expert**
✅ Requisitos da fase avançado + criar um endpoint para pesquisar produtos utilizando qualquer campo, incluindo parte da descrição da categoria (restrito à role ADMIN).

---

## 🚀 Funcionalidades
- Gerenciar produtos e categorias em um e-commerce.
- Implementação de autenticação JWT para controle de acesso.
- Pesquisa avançada de produtos por diferentes critérios.

---

## ⚙️ Requisitos
- Java 11 ou superior
- Maven

---

## 🛠️ Tecnologias Utilizadas
- **Spring Boot** (Framework principal)
- **Spring Data JPA** (Persistência de dados)
- **RESTful APIs** (Padrão de comunicação)
- **Lombok** (Redução de boilerplate code)
- **H2 Database** (Banco de dados em memória para desenvolvimento)
- **Spring Security + JWT** (Autenticação e autorização)

---

## 💻 Instalação e Configuração
1. **Clone o repositório**:
   ```bash
   git clone https://github.com/igoralves367/alterdata-desafio-backend.git

2. **Collections do Postman**:
Para facilitar os testes da API, utilize a Collection do Postman disponível no repositório.

📌 Baixe a Collection do Postman aqui: 
 ```bash
🔗 https://github.com/igoralves367/alterdata-desafio-backend/blob/develop/AltterDataDesafio.postman_collection.json

- Importe a collection no Postman para testar os endpoints da API.
- Inclui exemplos de requisições para CRUD de produtos, categorias e autenticação JWT
