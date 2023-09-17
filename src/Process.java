public class Process {

    private Long id;
    private boolean isCoordinator = false;

    public Process(Long id){
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public boolean isCoordinator() {
        return isCoordinator;
    }

    public void makeCoordinator(){
        this.isCoordinator = true;
    }
}
