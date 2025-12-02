# Comparação dos Logs -- Carro.java vs CarroLock.java

## 1. Introdução

Este documento apresenta uma análise comparativa entre os arquivos de
log **Carro.java** e **CarroLock.java**, destacando diferenças de
comportamento relacionadas à concorrência e sincronização no
gerenciamento de vagas.

## 2. Observações Gerais

### Carro.java

Log: mostra **interferência de threads**, com carros ocupando e
liberando vagas de forma desordenada.\
Exemplos:\
- Um carro libera a vaga logo após outro ocupá‑la, indicando possível
condição de corrida.\
- A ordem de ocupação e liberação é inconsistente.

### CarroLock.java

Log: mostra comportamento **muito mais ordenado**, demonstrando controle
adequado de acesso concorrente.\
Exemplos:\
- Todos os carros de 0 a 19 ocupam vagas antes de qualquer liberação.\
- As liberações ocorrem apenas após todas as ocupações concluírem,
indicando sincronização correta.

------------------------------------------------------------------------

## 3. Diferenças Evidentes

### 3.1 Ordem dos eventos

-   **Carro.java**: A liberação ocorre de forma intercalada com novas
    ocupações.\
-   **CarroLock.java**: Primeiro todas as ocupações acontecem, depois as
    liberações.

### 3.2 Concorrência

-   **Carro.java** exibe comportamento típico de código **não
    sincronizado**, com riscos de:
    -   Dupla ocupação da mesma vaga,
    -   Liberação de vaga não ocupada,
    -   Ordem imprevisível dos eventos.
-   **CarroLock.java** usa mecanismos de lock/mutex, garantindo:
    -   Exclusão mútua,
    -   Acesso seguro às vagas,
    -   Ordem coerente e determinística.

### 3.3 Confiabilidade

-   **CarroLock.java** é claramente mais seguro para ambientes
    multithread.\
-   **Carro.java** pode gerar inconsistências, especialmente em cenários
    de alto paralelismo.

------------------------------------------------------------------------

## 4. Conclusão

A comparação evidencia que o uso de mecanismos de **Lock** no programa
*CarroLock.java* torna o gerenciamento de vagas mais seguro, previsível
e livre de condições de corrida. Os logs confirmam que o controle de
concorrência foi bem implementado, garantindo consistência nas
operações.
