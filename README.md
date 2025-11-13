## Concorrente

# Relatório de Análise das Execuções de Concorrência em Java


## Execução 1 
**Log:**
- Conta criada com saldo zero.
- Threads iniciam: Mãe, Filho, Pai.
- Todas tentam sacar com saldo insuficiente.

**Observações:**
- A ordem de ativação foi: Mãe → Filho → Pai.
- Cada thread detectou saldo insuficiente e efetuou um saque de R$0,00.
- O padrão de execução é linear e previsível, indicando uma possível maior estabilidade na ordem de escalonamento do sistema para esta execução específica.

**Interpretação:**
Nesta execução, as threads foram ativadas em sequência sem interleaving significativo. Embora não haja sincronização, o comportamento aparenta ser ordenado, mas isso é apenas coincidência da ordem de escalonamento na CPU.

---

## Execução 2 
**Log:**
- Conta criada com saldo zero.
- Ordem de ativação: Pai → Filho → Mãe.
- Cada thread verifica saldo insuficiente e realiza saque de R$0,00.

**Observações:**
- A ordem de execução mudou em relação ao Log 1, demonstrando o comportamento não determinístico típico de operações concorrentes.
- O thread "Pai" foi ativado imediatamente após a criação da conta, diferentemente da Execução 1.
- O thread "Filho" realizou a impressão tanto do estado de saque quanto do valor sacado, enquanto "Pai" teve a impressão de saque somente ao final.

**Interpretação:**
A execução deixa claro que a falta de sincronização permite que a ordem de execução das threads varie amplamente, produzindo resultados de log diferentes embora o saldo final seja o mesmo.

---

## Execução 3 
**Log:**
- Conta criada com saldo zero.
- Ordem de ativação: Filho → Pai → Mãe.
- "Filho" realizou tanto o aviso de saldo insuficiente quanto o saque completo antes das demais threads iniciarem.
- Pai e Mãe tiveram suas mensagens parcialmente intercaladas.

**Observações:**
- Esta execução apresenta maior interleaving das threads, evidenciando concorrência mais evidente.
- A thread "Pai" foi ativada, mas sua mensagem de saque foi exibida apenas após a conclusão da ação da Mãe.
- A ordem real de execução dos métodos internos pode não refletir totalmente a ordem de impressão, já que prints podem ser intercalados pelo scheduler.

**Interpretação:**
Este log demonstra de forma clara a ausência de controle de acesso ao recurso compartilhado, resultando em comportamento imprevisível entre as threads.

---

## Comparativo Entre as Três Execuções
| Execução | Primeira Thread Ativa | Última Thread a Registrar Saque | Interleaving | Evidência de Não Determinismo |
|----------|------------------------|----------------------------------|--------------|-------------------------------|
| 1        | Mãe                    | Pai                              | Baixo        | Sim                           |
| 2        | Pai                    | Pai                              | Médio        | Sim                           |
| 3        | Filho                  | Pai                              | Alto         | Sim                           |

**Pontos em comum:**
- Saldo inicial zero sempre resulta em saques de R$0,00.
- Todas as threads identificam saldo insuficiente.
- Não há sincronização e isso causa mudanças na ordem de execução.

**Pontos divergentes:**
- Ordem de ativação das threads.
- Interleaving entre mensagens de saída.
- Tempo relativo entre ativação e encerramento de cada thread.

---

## Conclusão Geral
As três execuções demonstram comportamentos distintos devido à ausência de mecanismos de sincronização. Em um ambiente concorrente, threads competem pelo tempo de CPU e seu comportamento torna-se não determinístico. Mesmo com operações simples e saldo zero, a ordem das mensagens e o fluxo de execução variam significativamente.

Caso o programa necessite de consistência no acesso ao recurso compartilhado (como a conta bancária), é essencial utilizar técnicas como:
- `synchronized` em métodos críticos
- Locks explícitos (`ReentrantLock`)
- Monitores
- Estruturas atômicas (`AtomicInteger`, etc.)

Com sincronização adequada, o comportamento seria previsível, garantindo consistência no resultado, ainda que a ordem de execução permaneça dependente do escalonador.

---

## Considerações Finais
O experimento evidencia com clareza a importância de tratar corretamente acesso concorrente a recursos compartilhados. A ausência de sincronização não apenas altera a ordem de execução, mas também pode levar, em cenários reais, a inconsistências severas e falhas lógicas.




## Lock

# Relatório de Análise das Execuções com Uso de Lock


## Execução 1 
### Observações Importantes
- As contas são criadas com saldo inicial adequado.
- As threads iniciam quase simultaneamente: Mãe, Filho, Pai e Filho 2.
- O uso de Lock garante que os saques sejam realizados de forma exclusiva, evitando que duas threads acessem simultaneamente o saldo.

### Eventos Relevantes
- **Mãe** realiza o primeiro saque com sucesso: R$100,00 → saldo vai a R$0,00.
- **Filho** e **Pai** tentam sacar, mas não há saldo → ambos sacam R$0,00.
- **Filho 2** realiza dois saques de R$100,00 cada, retirando valores da segunda conta (que tinha saldo R$200,00).
- Saques adicionais que excedem o saldo resultam em operações negadas.

### Interpretação
Apesar de haver múltiplas tentativas simultâneas, o Lock assegura que cada saque ocorra de forma isolada. A sequência de saques varia pela ordem de agendamento das threads, mas o saldo nunca fica negativo.

---

## Execução 2 
### Observações Importantes
- Ordem de ativação muda: Mãe → Filho 2 → Filho → Pai.
- A lógica de saque se mantém consistente com o controle por Lock.

### Eventos Relevantes
- **Mãe** novamente realiza o primeiro saque de R$100,00.
- **Filho** e **Pai** encontram saldo zerado e sacam R$0,00.
- **Filho 2** realiza dois saques sequenciais de R$100,00 cada, retirando totalmente o saldo da segunda conta.

### Interpretação
A ordem muda, mas o comportamento é previsível. O Lock impede erros mesmo com interleaving alto.

---

## Execução 3 
### Observações Importantes
- Ordem de ativação: Filho → Mãe → Filho 2 → Pai.
- **Filho** realiza o primeiro saque com sucesso na conta de R$100,00.
- **Filho 2** domina os saques na conta de R$200,00.

### Eventos Relevantes
- Saldo fica zerado após o primeiro saque do Filho.
- Demais tentativas de Mãe e Pai não conseguem sacar.
- **Filho 2** novamente realiza dois saques seguidos de R$100,00.

### Interpretação
O comportamento é consistente e previsível, mesmo com ordem de execução alterada. O Lock garante integridade total.

---

## Comparativo Entre as Execuções
| Execução | Primeiro a Sacar | Conta 1 (R$100) | Conta 2 (R$200) | Interleaving | Evidência de Sincronização |
|----------|-------------------|------------------|------------------|--------------|-----------------------------|
| 1        | Mãe               | Zerada por Mãe   | Zerada por Filho 2 | Alto         | Sim                         |
| 2        | Mãe               | Zerada por Mãe   | Zerada por Filho 2 | Alto         | Sim                         |
| 3        | Filho             | Zerada por Filho | Zerada por Filho 2 | Alto         | Sim                         |

---

## Conclusões Gerais
As execuções demonstram que o uso de **Locks** elimina completamente problemas de condição de corrida. Mesmo com múltiplas threads competindo pelo mesmo recurso, os seguintes benefícios são evidentes:

- **Integridade de dados garantida**: nenhum saldo fica negativo.
- **Acesso exclusivo ao recurso crítico**: apenas uma thread por vez realiza saques.
- **Determinismo parcial**: embora a ordem de execução varie, os resultados são consistentes.
- **Maior previsibilidade**: comportamento estável apesar da concorrência elevada.

---

## Considerações Finais
A comparação entre os cenários com e sem Lock evidencia a importância do controle de acesso em ambientes multithread. Sem sincronização, logs mostram execuções imprevisíveis; com Locks, o sistema se comporta de maneira muito mais controlada e confiável.


## RwLock

# Relatório de Análise das Execuções com Uso de ReadWriteLock


## Execução 1 
### Observações Importantes
- A conta é criada com saldo inicial de R$100,00.
- As threads iniciam: Filho → Pai → Mãe.
- O primeiro saque bem-sucedido ocorre pelo cliente **Pai**, que retira R$100,00.

### Eventos Relevantes
- Após o saque do Pai, o saldo vai a R$0,00.
- Tentativas de saque subsequentes de Mãe e Filho resultam em valores de R$0,00.
- As mensagens indicam que o saldo não é lido simultaneamente por múltiplas threads durante o saque, reforçando a ação correta do lock de escrita.

### Interpretação
O RwLock permite que múltiplas leituras ocorram simultaneamente, mas o saque exige escrita e, portanto, o bloqueio exclusivo é ativado. O comportamento é consistente e evita condições de corrida.

---

## Execução 2 
### Observações Importantes
- Ordem de ativação: Pai → Mãe → Filho.
- Assim como no Log 1, **Pai** realiza o único saque bem-sucedido.

### Eventos Relevantes
- Mãe e Filho executam apenas leitura seguida de tentativa de saque sem saldo.
- Ambas as tentativas resultam em saque de R$0,00.
- O comportamento segue praticamente idêntico ao da primeira execução.

### Interpretação
Apesar da ordem diferente das threads, o resultado é igual ao da Execução 1. Isso reforça que o mecanismo de RwLock garante previsibilidade no comportamento do sistema, mesmo com alterações no escalonamento.

---

## Execução 3 
### Observações Importantes
- Ordem de ativação: Pai → Mãe → Filho.
- O Pai novamente realiza o único saque válido.

### Eventos Relevantes
- Mãe e Filho não conseguem sacar devido ao saldo zerado.
- A leitura simultânea do saldo é permitida, mas a escrita permanece exclusiva.
- O comportamento se mantém consistente com as execuções anteriores.

### Interpretação
O RwLock preserva a integridade dos dados e evita duplicidade de saques, garantindo que apenas um cliente consiga alterar o saldo enquanto os demais apenas o leem.

---

## Comparativo Entre as Execuções
| Execução | Primeiro a Sacar | Saldo Inicial | Saldo Final | Interleaving | Evidência de RwLock |
|----------|-------------------|----------------|--------------|--------------|-----------------------|
| 1        | Pai               | R$100,00       | R$0,00       | Médio        | Sim                   |
| 2        | Pai               | R$100,00       | R$0,00       | Baixo        | Sim                   |
| 3        | Pai               | R$100,00       | R$0,00       | Baixo        | Sim                   |

**Pontos em Comum:**
- Pai sempre realiza o único saque válido.
- Mãe e Filho nunca conseguem sacar.
- RwLock impede qualquer inconsistência ou saque duplicado.

**Diferenças:**
- Apenas a ordem de ativação das threads muda.
- O padrão de mensagens tem pequenas variações no interleaving, mas o comportamento lógico é o mesmo.

---

## Conclusões Gerais
O uso de **ReadWriteLock** proporciona forte consistência no fluxo de operações:
- **Leituras simultâneas** são permitidas sem impacto negativo.
- **Escritas** permanecem totalmente protegidas e não concorrentes.
- **O resultado final é sempre igual nas três execuções**, reforçando a estabilidade do modelo.

Embora, nesse cenário específico, o benefício do RwLock em relação ao Lock tradicional seja sutil (devido ao fluxo simples de leitura/saque), a abordagem se torna muito mais vantajosa em sistemas com alta densidade de leituras.

---

## Considerações Finais
O mecanismo de RwLock demonstrou comportamento previsível, seguro e coerente em todas as execuções. Isso evidencia que, para sistemas onde leituras são frequentes e escritas são pontuais, o uso de bloqueios diferenciados pode fornecer melhor desempenho sem comprometer a integridade dos dados.


## Synk

# Relatório de Análise das Execuções com Uso de `synchronized`


## Execução 1 
### Observações Importantes
- Conta criada com saldo de R$100,00.
- **Pai** realiza o único saque possível, retirando R$100,00.
- Em seguida, Mãe e Filho recebem mensagens de saldo insuficiente.

### Eventos Relevantes
- A operação de saque do Pai é concluída antes de qualquer outra tentativa ser processada.
- As demais threads entram na região sincronizada somente após o término da execução do Pai.
- O saldo nunca assume valores inconsistentes e não há duplicidade de saques.

### Interpretação
O uso do **`synchronized`** garante que apenas uma thread execute o método de saque por vez. Isso impede condições de corrida e garante a integridade do saldo.

---

## Execução 2 
### Observações Importantes
- A execução segue o mesmo padrão da Execução 1.
- O Pai novamente é o primeiro a realizar um saque, consumindo o saldo por completo.

### Eventos Relevantes
- Mãe e Filho entram na região sincronizada posteriormente e encontram saldo insuficiente.
- Nenhuma inconsistência é registrada.

### Interpretação
O comportamento confirma a previsibilidade gerada pelo uso de sincronização. A ordem das threads pode variar, mas a exclusão mútua preserva o resultado final.

---

## Execução 3 
### Observações Importantes
- Assim como nas execuções anteriores, o Pai realiza o único saque válido.
- As demais threads apenas tentam sacar e depois registram operação de saque total de R$0,00.

### Eventos Relevantes
- A sincronização impede que mais de um saque seja efetuado.
- Comportamento idêntico ao observado anteriormente.

### Interpretação
A repetição exata da lógica das execuções anteriores evidencia a estabilidade do modelo quando `synchronized` é aplicado.

---

## Comparativo Entre as Execuções
| Execução | Primeiro a Sacar | Saldo Inicial | Saldo Final | Interleaving | Evidência de Sincronização |
|----------|-------------------|----------------|--------------|--------------|-----------------------------|
| 1        | Pai               | R$100,00       | R$0,00       | Baixo        | Sim                         |
| 2        | Pai               | R$100,00       | R$0,00       | Muito baixo  | Sim                         |
| 3        | Pai               | R$100,00       | R$0,00       | Muito baixo  | Sim                         |

**Pontos em Comum:**
- Pai sempre realiza o único saque válido.
- Mãe e Filho sempre encontram saldo insuficiente.
- A sincronização evita qualquer inconsistência ou comportamento inesperado.

**Diferenças:**
- Pequenas mudanças na ordem de ativação das threads.
- Saídas dos logs exibem leve variação na impressão, mas o comportamento interno é idêntico.

---

## Conclusões Gerais
O uso do **`synchronized`** proporciona:
- **Exclusão mútua** total durante operações críticas.
- **Integridade garantida** do saldo.
- **Comportamento totalmente previsível**, independentemente da ordem de execução das threads.

Apesar de simples, `synchronized` é suficiente para cenários onde há baixa complexidade de leitura e escrita e poucas threads competindo simultaneamente.

---

## Considerações Finais
As três execuções demonstram um comportamento idêntico graças à exclusão mútua. Não há variação no resultado final e nenhuma inconsistência ocorre. Assim, o mecanismo `synchronized` provou ser totalmente eficaz para o controle concorrente proposto neste cenário.