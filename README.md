API de Chamados de TI

API REST para registro e acompanhamento de chamados de suporte técnico.

Um funcionário abre um chamado descrevendo o problema e indicando a prioridade. O chamado nasce aberto e pode ser fechado depois, registrando a data de fechamento. Chamados já fechados não podem ser fechados novamente.

Stack
Java 21
Spring Boot 4.1.1 (Spring Web, Spring Data JPA, Bean Validation)
PostgreSQL
Maven
Endpoints
Método	Rota	Descrição	Respostas
POST	/chamados	Registra um chamado novo	200, 400 se a validação falhar
GET	/chamados	Lista todos os chamados	200
GET	/chamados/abertos	Lista apenas os chamados abertos	200
PUT	/chamados/{id}/fechar	Fecha um chamado	200, 404 se não existir, 409 se já estiver fechado
Exemplo de requisição
http
POST /chamados
Content-Type: application/json

{
  "prioridade": "ALTA",
  "problema": "Impressora do setor financeiro não imprime"
}

O status, a dataAbertura e o id são definidos pela aplicação — não são aceitos na requisição.

Como executar
1. Criar o banco

O Hibernate cria as tabelas, mas não cria o banco. Antes de subir a aplicação, crie um banco vazio no PostgreSQL:

sql
CREATE DATABASE chamados;

Ou pelo pgAdmin: botão direito em Databases → Create → Database...

2. Configurar as credenciais

O arquivo application.properties não é versionado, porque contém a senha do banco. Copie o modelo e preencha:

bash
cp src/main/resources/application-example.properties src/main/resources/application.properties

Preencha spring.datasource.username e spring.datasource.password.

3. Subir
bash
./mvnw spring-boot:run

No Windows:

mvnw.cmd spring-boot:run

A aplicação sobe em http://localhost:8080. As tabelas são criadas automaticamente pelo Hibernate na primeira execução.

Decisões de projeto

Não existe entidade Funcionario. Nenhum requisito exige consulta, relacionamento ou regra de negócio sobre funcionários — o solicitante seria apenas um atributo do chamado. Uma entidade se justifica por um requisito, não por existir no mundo real. Caso autenticação seja adicionada, essa decisão deve ser revista: aí o funcionário passaria a ter permissões e o chamado precisaria referenciá-lo.

PostgreSQL em vez de H2. O sistema mantém histórico de chamados, que precisa sobreviver ao desligamento da aplicação. Banco em memória serve para projetos descartáveis, não para este caso.

API REST em vez de páginas renderizadas. Os requisitos descrevem operações sobre recursos, sem nenhuma exigência de interface. Uma camada de apresentação pode ser adicionada depois consumindo estes mesmos endpoints.

DTO na entrada (ChamadoRequest). Expor a entidade diretamente no @RequestBody permitiria ao cliente enviar "status": "FECHADO" e criar um chamado que já nasce fechado, contornando a regra. O DTO aceita apenas prioridade e problema; o construtor da entidade garante ABERTO e a data de abertura.

Filtro de abertos via método derivado, não via Stream. findByStatus(Status) gera um WHERE no banco. Filtrar com filter no Service traria todos os registros pela rede para descartar a maioria em memória — aceitável com dezenas de linhas, custoso com milhares. O filtro deve acontecer o mais perto do dado possível.

As regras de negócio estão no Service. "Não fechar um chamado já fechado" é uma verdade sobre o domínio, não sobre HTTP. Se esta API fosse substituída por uma interface web ou um aplicativo, a regra continuaria valendo sem alteração.

409 Conflict para chamado já fechado. A requisição é válida e o recurso existe — o que impede a operação é o estado atual do chamado. 400 Bad Request sugeriria requisição malformada, o que não descreve o caso.

Exceções com @ResponseStatus. ChamadoNaoEncontradoException mapeia para 404 e ChamadoJaFechadoException para 409. Sem essa anotação, ambas resultariam em 500, que indicaria falha do servidor quando na verdade a aplicação funcionou corretamente.

Limitações conhecidas

Mensagens de validação não chegam ao cliente. Requisições inválidas retornam 400, mas o corpo traz apenas timestamp, status, error e path — as mensagens definidas em @NotBlank e @NotNull ficam apenas no log. A solução é um @RestControllerAdvice com um @ExceptionHandler para MethodArgumentNotValidException, convertendo os erros de campo em um JSON legível. Não implementado.

Sem testes automatizados. O projeto foi validado manualmente via Postman. Faltam testes unitários do ChamadoService, especialmente do método fechar, que concentra a lógica de negócio.

Sem paginação. GET /chamados retorna todos os registros. Com volume real, seria necessário Pageable.

Sem DTO de saída. As respostas serializam a entidade diretamente, o que acopla o contrato da API ao modelo de persistência: qualquer alteração na entidade muda a resposta para os clientes.

## Testes

Testes unitários do `ChamadoService`, escritos com **JUnit 5** e **Mockito**.

O `ChamadoRepository` é substituído por um mock, então os testes **não precisam do PostgreSQL** ligado nem do banco criado.

### O que é testado

| Teste | Cenário | O que confere |
|---|---|---|
| `deveFecharChamadoAberto` | Fechar um chamado que está aberto | Status passa a `FECHADO`, data de fechamento é preenchida e o chamado é salvo |
| `deveLancarExcecaoAoFecharChamadoJaFechado` | Tentar fechar um chamado já fechado | Lança `ChamadoJaFechadoException` e não salva o chamado uma segunda vez |

### Validação dos testes

Cada teste foi validado introduzindo um bug proposital no `ChamadoService` e confirmando que ele falha:

- **Removendo o `save`**: os dois testes falham. Sem a verificação do `save`, o teste do caminho feliz passaria mesmo com o chamado nunca sendo gravado, porque o objeto é alterado em memória.
- **Removendo a regra de "já fechado"**: o teste do caminho de erro falha.

### Como rodar

Pelo terminal, na raiz do projeto:

```bash
./mvnw test      # Linux / Mac
.\mvnw test      # Windows
```

Pela IDE: botão direito em `ChamadoServiceTest` → Run As → JUnit Test.

### Não coberto

- `registrarChamado`, `listarTodos` e `listarAbertos`
- Controller e códigos HTTP
- Teste de integração com o banco


Autor

João Marcos —

