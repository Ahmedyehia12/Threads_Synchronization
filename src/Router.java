public class Router {
    private int size;
    Router(int size) {
        this.size = size;
    }
    private Object connections [] = new Object[size];
    private Semaphore sem = new Semaphore(size);
    int ind = 0;
    public void connect(Object o) {
        sem.P(); // wait for space
        // find first empty slot
        for(int i = 0; i < size; i++) {
            if(connections[i] == null) {
                connections[i] = o;
                return;
            }
        }
    }
    public void release(Object o) {
        for(int i = 0; i < size; i++) {
            if(connections[i] == o) {
                connections[i] = null;
                sem.V(); // signal that there is space
                return;
            }
        }
    }
}
