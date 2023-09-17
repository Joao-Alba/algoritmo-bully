import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static List<Process> processList = new ArrayList<>();
    public static final Lock idLock = new ReentrantLock(true);
    public static final Lock electionLock = new ReentrantLock(true);


    public static void main(String[] args) {
        System.out.println("Iniciando threads");
        startThreads();
    }

    private static void startThreads(){
        CreateProcessRunnable createProcessRunnable = new CreateProcessRunnable();
        Thread createProcessThread = new Thread(createProcessRunnable);
        createProcessThread.start();

        CreateRequestRunnable createRequestRunnable = new CreateRequestRunnable();
        Thread createRequestThread = new Thread(createRequestRunnable);
        createRequestThread.start();

        KillProcessRunnable killProcessRunnable = new KillProcessRunnable();
        Thread killProcessThread = new Thread(killProcessRunnable);
        killProcessThread.start();

        KillCoordinatorRunnable killCoordinatorRunnable = new KillCoordinatorRunnable();
        Thread killCoordinatorThread = new Thread(killCoordinatorRunnable);
        killCoordinatorThread.start();
    }

    public static void startElection(Process requesterProcess){
        try {
            electionLock.lock();
            for(Process process : processList){
                if(!process.getId().equals(requesterProcess.getId()) && requesterProcess.getId() < process.getId()){
                    System.out.println("Processo #" + requesterProcess.getId() + " chama processo #" + process.getId());
                    System.out.println("Processo #" + process.getId() + " responde OK!");
                    startElection(process);
                    return;
                }
            }
            requesterProcess.makeCoordinator();
            System.out.println("Eleição finalizada -> Novo coordenador: #" + requesterProcess.getId());
        }finally {
            electionLock.unlock();
        }
    }
}