public class Semaphore {
    protected  int value;
    public Semaphore() { value = 0; }
    public Semaphore(int initial) { this.value = initial; }
    public synchronized void P(){
        value--; // decrement
        if(value<0){ // if value is negative
            try{
                wait(); // block
            }catch (InterruptedException e){ // catch exception
                System.out.println("InterruptedException caught"); // print message
            }
        }
    }

    public  synchronized void V(){
        value++;
        if(value<=0){
            notify();
        }
    }

}
