# Plataforma de Doações Locais

**PROJETO INTEGRADOR V: ANÁLISE DE SOLUÇÕES INTEGRADAS PARA ORGANIZAÇÕES**

Uma plataforma web que conecta doadores, instituições sociais e pessoas em situação de vulnerabilidade para facilitar a doação de alimentos de forma local, prática e transparente.

## 🎯 Sobre o Projeto

A plataforma visa otimizar o processo de doações, garantindo que os alimentos encontrem as pessoas que mais precisam deles. Através da conexão direta entre doadores e beneficiários, o projeto promove:

- **Impacto direto**: Doações chegam a quem realmente precisa
- **Comunidade local**: Foco em proximidade geográfica para facilitar entregas
- **Transparência**: Acompanhamento do destino das doações
- **Eficiência**: Redução do desperdício e otimização de recursos

### 👥 Integrantes do Grupo

- **David Marao Spungin**
- **Iago Pedro da Silva**
- **Kalani Klafke de Lemos Perin**
- **Sidney de Oliveira Junior**
- **Thiago Petrucelli de Lira**
- **Wagner Morais Freitas da Silva**

---

## 🚀 Tecnologias Utilizadas

### Backend
- **Java 17** - Linguagem de programação
- **Spring Boot 3.1.5** - Framework principal
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **JWT** - Autenticação baseada em tokens
- **MySQL** - Banco de dados
- **Maven** - Gerenciamento de dependências

### Frontend
- **Angular 20** - Framework frontend
- **Angular Material** - Biblioteca de componentes UI
- **TypeScript** - Linguagem de programação
- **RxJS** - Programação reativa
- **SCSS** - Preprocessador CSS

---

## 📁 Estrutura do Projeto

```
IntegradorSenac2025/
├── frontend/                    # Aplicação Angular
│   ├── src/
│   │   ├── app/                # Componentes da aplicação
│   │   ├── environments/       # Configurações de ambiente
│   │   └── index.html          # Página principal
│   ├── package.json            # Dependências do Node.js
│   └── angular.json            # Configuração do Angular
│
├── donations-api/              # API REST Spring Boot
│   ├── src/
│   │   ├── main/java/          # Código fonte Java
│   │   └── main/resources/     # Recursos e configurações
│   └── pom.xml                 # Dependências do Maven
│
└── README.md                   # Este arquivo
```

---

## ⚙️ Pré-requisitos

Antes de executar a aplicação, certifique-se de ter instalado:

- **Java 17+** - [Download](https://openjdk.org/projects/jdk/17/)
- **Maven 3.6+** - [Download](https://maven.apache.org/download.cgi)
- **Node.js 18+** - [Download](https://nodejs.org/)
- **Angular CLI** - `npm install -g @angular/cli`
- **MySQL 8.0+** - [Download](https://dev.mysql.com/downloads/mysql/)

---

## 🔧 Configuração e Execução

### 1. Configuração do Banco de Dados

**Configuração Simples (padrão do projeto):**

1. **Instale o MySQL** e certifique-se de que está rodando na porta 3306
2. **Conecte como root** (sem senha) e execute:
   ```sql
   CREATE DATABASE donations_db;
   ```

**✅ Pronto!** O Spring Boot fará o resto automaticamente:
- Criará as tabelas via JPA
- Populará os dados via `DataInitializer`

**⚠️ Se sua instalação do MySQL usar senha para root:**
Edite o arquivo `donations-api/src/main/resources/application.properties`:
```properties
spring.datasource.password=SUA_SENHA_AQUI
```

### 2. Executando o Backend

```bash
# Navegar para o diretório da API
cd donations-api

# Instalar dependências e executar
mvn clean install
mvn spring-boot:run
```

O backend estará disponível em: `http://localhost:8080`

**✅ Verificação**: 
- Se a aplicação iniciar sem erros e você ver logs do tipo:
  ```
  INFO - DataInitializer: Criando usuários de teste...
  INFO - DataInitializer: 10 usuários criados com sucesso!
  ```

Significa que o banco foi configurado corretamente!

### 3. Executando o Frontend

```bash
# Navegar para o diretório do frontend
cd frontend

# Instalar dependências
npm install

# Executar em modo de desenvolvimento
npm start
```

O frontend estará disponível em: `http://localhost:4200`

---

## 🎨 Funcionalidades Principais

### Para Doadores
- Cadastro e autenticação segura
- Publicação de itens para doação
- Filtro por localização e categoria
- Agendamento de retirada
- Acompanhamento do status das doações

### Para Beneficiários
- Busca por itens necessários
- Filtro por localização e tipo de necessidade
- Solicitação de doações
- Acompanhamento de entregas

### Para Instituições (ONGs)
- Gestão de doações recebidas
- Intermediação entre doadores e beneficiários
- Relatórios de impacto
- Divulgação de necessidades em tempo real

---

## 🔐 Autenticação

A aplicação utiliza JWT (JSON Web Tokens) para autenticação:

1. **Registro/Login**: Usuário fornece credenciais
2. **Token JWT**: Gerado e retornado na resposta
3. **Autorização**: Token incluído no header `Authorization: Bearer <token>`
4. **Roles**: Controle de acesso baseado em perfis (DONOR, REQUESTER, ADMIN)

---

## 📊 Dados de Teste

A aplicação inclui um sistema de inicialização automática (`DataInitializer`) que popula o banco com dados de exemplo na primeira execução:

- **10 usuários** (doadores, beneficiários e admins)  
- **5 doações** de diferentes categorias
- **2 matches** entre doadores e beneficiários

**Usuários de teste disponíveis:**
- **João Silva Santos** - `joao.silva@email.com` / Senha: `123456` (DONOR)
- **Maria Fernanda Costa** - `maria.costa@email.com` / Senha: `123456` (DONOR)
- **Lúcia Maria Santos** - `lucia.santos@email.com` / Senha: `123456` (REQUESTER)
- **Carlos Eduardo Rocha** - `carlos.rocha@email.com` / Senha: `123456` (ADMIN)

**⚠️ Nota**: Os dados são criados automaticamente apenas na primeira execução. Se precisar recriar os dados, apague o banco e reinicie a aplicação.

---

## 📚 Documentação da API (Swagger)

A API inclui documentação interativa completa através do **Swagger UI**, permitindo visualizar e testar todos os endpoints disponíveis.

### 🔗 Acessando o Swagger

Após iniciar o backend, acesse:

**Swagger UI**: `http://localhost:8080/swagger-ui/index.html`

**OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

### 🔐 Como usar a autenticação no Swagger

1. **Fazer Login**: Use o endpoint `/api/auth/login` com um dos usuários de teste
2. **Copiar o Token**: Copie o valor do campo `token` da resposta (sem o "Bearer")
3. **Autorizar**: Clique no botão **"Authorize"** 🔒 no topo da página
4. **Inserir Token**: Cole o token no campo `bearerAuth` e clique em **"Authorize"**
5. **Testar Endpoints**: Agora você pode testar endpoints protegidos

### 📋 Principais Endpoints Disponíveis

#### **Autenticação**
- `POST /api/auth/register` - Registrar novo usuário
- `POST /api/auth/login` - Fazer login e obter token JWT

#### **Usuários**
- `GET /api/users/profile` - Obter perfil do usuário logado
- `PUT /api/users/profile` - Atualizar perfil do usuário

#### **Doações**
- `GET /api/donations` - Listar doações disponíveis
- `POST /api/donations` - Criar nova doação
- `GET /api/donations/{id}` - Obter detalhes de uma doação
- `PUT /api/donations/{id}` - Atualizar doação (apenas proprietário)
- `DELETE /api/donations/{id}` - Excluir doação (apenas proprietário)

#### **Matches**
- `POST /api/matches` - Solicitar uma doação
- `GET /api/matches/my-requests` - Minhas solicitações
- `GET /api/matches/my-donations` - Solicitações para minhas doações

### 💡 Exemplo de Teste Rápido

1. **Acesse**: `http://localhost:8080/swagger-ui/index.html`
2. **Login**: Use `joao.silva@email.com` / `123456` no endpoint `/api/auth/login`
3. **Autorize**: Copie o token e autorize no Swagger
4. **Teste**: Experimente o endpoint `GET /api/donations` para ver as doações

---

## 🤝 Contribuição

Para contribuir com o projeto:

1. Faça um fork do repositório
2. Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

---

## 📝 Licença

Este projeto foi desenvolvido como parte do **Projeto Integrador V** do curso de **Tecnologia em Análise e Desenvolvimento de Sistemas** do **SENAC EAD**.

---

## 📞 Contato

Para dúvidas ou sugestões, entre em contato com a equipe de desenvolvimento através do repositório no GitHub.

**SENAC - Serviço Nacional de Aprendizagem Comercial**  
**EAD - Ensino à Distância - 2026**
