

public class Cliente extends Thread {
    private static Banco banco = new Banco();
    private Conta conta = null;
    private double valor = 100;

    public Cliente(String nome, Conta conta) {
        super(nome);
        this.conta = conta;
        System.out.println("Saldo da "+ nome+ "é" + conta.getSaldo());
    }
    
    public void run() {
        double total = 0;
        System.out.println("Cliente " + getName() + " ativo.");
        while (banco.saque(conta,valor)){
            total += valor;
        }
        System.out.println(getName() + " sacou total de R$" + total);
    }
}
