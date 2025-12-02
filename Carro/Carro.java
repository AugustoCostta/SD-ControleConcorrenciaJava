import java.util.concurrent.*;

public class Carro extends Thread {
    private static Semaphore estacionamento = new Semaphore(10);
    
    public Carro(String nome) { super(nome); }
    
    public void run() {
        try {
            estacionamento.acquire();
            System.out.println(getName() + " ocupou vaga.");
            // System.out.println(estacionamento.availablePermits() > 0 ? "Vagas livres: " + estacionamento.availablePermits() : "Estacionamento LOTADO");
            sleep((long)(Math.random() * 10000));
            System.out.println(getName() + " liberou vaga.");
            estacionamento.release();
            // System.out.println("Vagas livres: " + estacionamento.availablePermits());
        } catch(InterruptedException ie){ ie.printStackTrace(); }
    }
    
    public static void main(String args[]) {
        for (int i = 0; i< 20; i++)
            new Carro("Carro #"+i).start();
    }
}
