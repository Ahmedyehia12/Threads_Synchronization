import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Router {
    private final Device[] connections;
    private final int maxConnections;
    private final Semaphore semaphore;

    public Router(int n) {
        this.connections = new Device[n];
        this.maxConnections = n;
        this.semaphore = new Semaphore(n);
    }

    public void occupyConnection(Device d) {
        semaphore.acquire(d);
    }

    public void releaseConnection(Device d) {
        semaphore.release(d);
    }

    public Device[] getConnections() {
        return connections;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }
}

class Semaphore {
    private int value;

    public Semaphore(int n) {
        this.value = n;
    }

    public synchronized void acquire(Device d) {
        value--;
        if (value < 0) {
            System.out.println(d.getName() + " arrived and waiting");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else
            System.out.println(d.getName() + " arrived");


        for (int i = 0; i < d.getRouter().getMaxConnections(); i++) {
            if (d.getRouter().getConnections()[i] == null) {
                d.getRouter().getConnections()[i] = d;
                d.setConnectionID(i + 1);
                break;
            }
        }
        System.out.println("Connection " + d.getConnectionID() + ": " + d.getName() + " Occupied");
    }

    public synchronized void release(Device d) {
        value++;

        System.out.println("Connection " + d.getConnectionID() + ": " + d.getName() + " Logged out");
        for (int i = 0; i < d.getRouter().getMaxConnections(); i++) {
            if (d.getRouter().getConnections()[i] == d) {
                d.getRouter().getConnections()[i] = null;
                break;
            }
        }
        if (value >= 0)
            notifyAll();
    }

}

class Device implements Runnable {
    private final String name;
    private final String type;
    private final Router router;
    private int connectionID;

    public Device(String n, String t, Router r) {
        this.name = n;
        this.type = t;
        this.router = r;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Router getRouter() {
        return router;
    }

    public void setConnectionID(int id) {
        this.connectionID = id;
    }

    public int getConnectionID() {
        return connectionID;
    }


    @Override
    public void run() {
        router.occupyConnection(this); // hena by3ml print le arrived wa waiting wa occupied
        System.out.println("Connection " + connectionID + ": " + name + " login");

        try {
            Thread.sleep((long) (Math.random() * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Connection " + connectionID + ": " + name + " performs online activity");

//        System.out.println("Connection " + index + ": " + name + " Logged out"); tl3taha fel release wa 5leto bya5od device

        router.releaseConnection(this);


    }
}

class Network {
    public static void main(String[] args) {
        int N;
        int TC;
/*
test case
2
4
C1 mobile
C2 tablet
C3 pc
C4 pc
 */
        Scanner input = new Scanner(System.in);
        System.out.println("What is the number of WI-FI Connections?");
        N = input.nextInt();
        System.out.println("What is the number of devices Clients want to connect?");
        TC = input.nextInt();

        Router router = new Router(N);
        List<Device> devices = new ArrayList<>();

        for (int i = 0; i < TC; i++) {
            String name = input.next();
            String type = input.next();
            devices.add(new Device(name, type, router));
        }
        for (Device device : devices) {
            new Thread(device).start();
        }
    }
}
