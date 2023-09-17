import java.util.Optional;
import java.util.Random;

public class CreateRequestRunnable implements Runnable{

    public void run(){
        while(true){
            try {
                Thread.sleep(25000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            try{
                Main.electionLock.lock();

                if(Main.processList.isEmpty()){
                    continue;
                }

                Random rand = new Random();
                int index = rand.nextInt(Main.processList.size());
                Process requesterProcess = Main.processList.get(index);

                Optional<Process> coordinatorOpt = Main.processList.stream().filter(Process::isCoordinator).findFirst();

                coordinatorOpt.ifPresentOrElse(coordinator -> {
                    System.out.println("Processo #" + requesterProcess.getId() + " encontrou o coordenador #" + coordinator.getId());
                },
                () -> {
                    System.out.println("Processo #" + requesterProcess.getId() + " não encontrou o coordenador. Iniciando eleição");
                    Main.startElection(requesterProcess);
                });

            }finally {
                Main.electionLock.unlock();
            }
        }
    }
}