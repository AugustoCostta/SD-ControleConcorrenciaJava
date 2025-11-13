public class Conta {
    
    private double saldo = 0;
    
    public Conta(double saldo) {
        this.saldo = saldo;
        System.out.println("Conta criada. Saldo inicial: R$" + saldo);
    }
    
    public synchronized double getSaldo() {
        return saldo;
    }
    
    public synchronized void setSaldo(double saldo) {
        this.saldo = saldo;
    }
    
    public synchronized double debitarValor(double valor) {
        if (this.saldo < valor) {
            System.out.println("Saldo insuficiente para saque para " + Thread.currentThread().getName());
            return -1;
        } else {
            this.saldo -= valor;
            System.out.println("Saque realizado com sucesso para " + Thread.currentThread().getName());
            return this.saldo;
        }
    }
}
