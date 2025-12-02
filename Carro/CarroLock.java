import java.util.concurrent.locks.*;

public class CarroLock extends Thread {
  private Lock estacionamento = new ReentrantLock();
  
  public CarroLock(String nome) { 
    super(nome); 
  }
  
  public void run() {
  try {
    estacionamento.lock();
    System.out.println(getName() + " ocupou vaga.");
    sleep((long)(Math.random() * 10000));
    System.out.println(getName() + " liberou vaga.");
    estacionamento.unlock();
  } catch(InterruptedException ie){ 
    ie.printStackTrace(); 
  }
}

  public static void main(String args[]) {
    for (int i = 0; i< 20; i++)
    new CarroLock("Carro #"+i).start();
  }
}
