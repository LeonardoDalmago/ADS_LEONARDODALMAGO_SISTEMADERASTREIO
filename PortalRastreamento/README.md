# Portal de Rastreamento de Pedidos

Sistema web para cadastro e acompanhamento de pedidos. Os funcionários podem cadastrar clientes, transportadoras e pedidos, além de atualizar o andamento. O cliente consulta os pedidos pela página pública usando o CNPJ.

Projeto da disciplina de Análise e Desenvolvimento de Sistemas - ADS - (UPF).

## Tecnologias

- Java 21, Jakarta Faces (JSF) 4 e PrimeFaces 13
- Template Manhattan 7
- JPA com EclipseLink e PostgreSQL
- GlassFish 7 e NetBeans 21

## O que o sistema faz

- Login e controle de acesso para as áreas administrativa e de funcionários
- Cadastro, edição, exclusão, listagem e busca de usuários, clientes e transportadoras
- Cadastro de pedidos e atualização do histórico de andamento
- Consulta pública de pedidos por CNPJ (`/rastreio.xhtml`)
- Perfis de usuário e status do pedido
- Relacionamentos entre clientes, pedidos e histórico

## Como rodar

1. Instale JDK 21, NetBeans 21, GlassFish 7 e PostgreSQL.
2. Instale o Manhattan no Maven local e copie os arquivos do layout para `src/main/webapp` (veja o tutorial da disciplina).
3. No pgAdmin, crie o banco `portal_rastreamento` e execute `sql/01_script_banco.sql`
   (opcional: `sql/02_dados_teste.sql`).
4. Em `src/main/resources/META-INF/persistence.xml`, ajuste usuário e senha do seu Postgres.
5. Execute o projeto no GlassFish e acesse `http://localhost:8080/PortalRastreamento/`.

## Acessos

| Login                 | Senha    | Perfil |
| --------------------- | -------- | ------ |
| admin                 | admin123 | ADMIN  |
| func (dados de teste) | func123  | COMUM  |

CNPJ para testar o rastreamento público (dados de teste): `11.222.333/0001-81`.

## Estrutura

```
src/main/java/br/upf/portalrastreamento
  controller/   managed beans (CrudController, Login, Pedido, Rastreio...)
  converter/    conversores dos combos (Cliente, Transportadora)
  dao/          GenericDAO (salvar, excluir, consultar)
  entity/       entidades JPA
  enumeration/  Enums
  filter/       filtros de acesso (autenticação e administrativo)
  util/         JPAUtil, FacesUtil
src/main/webapp
  login.xhtml, rastreio.xhtml
  app/          pedidos (usuários logados)
  admin/        cadastros (somente ADMIN)
sql/            scripts do banco
```
