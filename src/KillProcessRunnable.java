import java.util.Optional;
import java.util.Random;

public class KillProcessRunnable implements Runnable{

    public void run(){
        while(true){
            try {
                Thread.sleep(80000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            try{
                Main.electionLock.lock();
                Main.idLock.lock();

                Random rand = new Random();
                int index = rand.nextInt(Main.processList.size());

                Process processToRemove = Main.processList.get(index);
                Main.processList.remove(processToRemove);
                System.out.println("Processo #" + processToRemove.getId() + " removido");
            }finally {
                Main.electionLock.unlock();
                Main.idLock.unlock();
            }
        }
    }
}