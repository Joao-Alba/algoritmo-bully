import java.util.Optional;

public class KillCoordinatorRunnable implements Runnable{

    public void run(){
        while(true){
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            try{
                Main.electionLock.lock();
                Main.idLock.lock();
                Optional<Process> coordinatorOpt = Main.processList.stream().filter(Process::isCoordinator).findFirst();

                coordinatorOpt.ifPresent(coordinator -> {
                    Main.processList.remove(coordinator);
                    System.out.println("Coordenador #" + coordinator.getId() + " removido");
                });
            }finally {
                Main.electionLock.unlock();
                Main.idLock.unlock();
            }
        }
    }
}