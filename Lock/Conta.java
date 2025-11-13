import java.util.concurrent.locks.*;

public class Conta {
     private double saldo = 0;
    private Lock lock = new ReentrantLock();
    
    /** Creates a new instance of Conta */
    public Conta(double saldo) {
        this.saldo = saldo;
        System.out.println("Conta criada. Saldo inicial: R$" + saldo);
    }
    
    public double getSaldo() {
        lock.lock();
        try {
            return saldo;
        } finally {
            lock.unlock();
        }
    }
    
    public void setSaldo(double saldo) {
        lock.lock();
        try {
            this.saldo = saldo;
        } finally {
            lock.unlock();
        }
    }
    
    public  double debitarValor(double valor) {
        lock.lock();
        try {
            if (this.saldo < valor) {
                System.out.println("Saldo insuficiente para saque para "+Thread.currentThread().getName());
                return -1;
            } else {
                this.saldo -= valor;
                 System.out.println("Cliente "+Thread.currentThread().getName()+" ativo."+" Saque no valor R$ "+valor+" realizado com sucesso!");
                return this.saldo;
            }
        } finally {
            lock.unlock();
        }
    }
}
