public class Semaphore {
    protected  int value = 0;
    protected Semaphore() { value = 0; }
    protected Semaphore(int initial) { value = initial; }
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
