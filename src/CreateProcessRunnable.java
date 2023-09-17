import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class CreateProcessRunnable implements Runnable{

    public void run(){
        while(true){
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Process process = new Process(this.generateProcessId());
            Main.processList.add(process);
            System.out.println("Processo criado: #" + process.getId());
        }
    }

    private Long generateProcessId(){
        try{
            Main.idLock.lock();
            Optional<Long> maxIdOpt = Main.processList.stream().map(Process::getId).max(Long::compareTo);

            AtomicReference<Long> nextId = new AtomicReference<>(0L);

            maxIdOpt.ifPresent(maxId -> {
                nextId.set(maxId + 1);
            });

            return nextId.get();
        }finally {
            Main.idLock.unlock();
        }

    }
}
