public class Conta {
    private double saldo = 0;

    public Conta() {
        System.out.println("Conta criada com saldo zero.");
    }

    public Conta(double saldo) {
        this.saldo = saldo;
        System.out.println("Conta criada. Saldo inicial: R$" + saldo);
    }

    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }
}
