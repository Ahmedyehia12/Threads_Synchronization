public class Device extends Thread {
    private Router router;
    private int id;
    public Device(Router router, int id) {
        this.router = router;
        this.id = id;
    }
    public void run() {
        while(true) {
            router.connect(this);
            System.out.println("Device " + id + " connected");
            try {
                Thread.sleep(1000);
                System.out.println("Device " + id + " is currently working on something");
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            router.release(this);
            System.out.println("Device " + id + " released");
        }
    }

}
