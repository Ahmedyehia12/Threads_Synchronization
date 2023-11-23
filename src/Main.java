import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


class FileHandler{

    public static void append(String data , String fileName){
        File file = new File(fileName);
        try {
            java.io.FileWriter fr = new java.io.FileWriter(file, true);
            fr.write(data);
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

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
            FileHandler.append(d.getName() + " arrived and waiting\n", "output.txt");
            try {
                wait(); // wait() methods places waiting thread into WAITING state, by pushing it into the waiting queue
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else
            FileHandler.append(d.getName() + " arrived\n", "output.txt");

        for (int i = 0; i < d.getRouter().getMaxConnections(); i++) {
            if (d.getRouter().getConnections()[i] == null) {
                d.getRouter().getConnections()[i] = d;
                d.setConnectionID(i + 1);
                break;
            }
        }
        FileHandler.append("Connection " + d.getConnectionID() + ": " + d.getName() + " Occupied\n","output.txt");
    }

    public synchronized void release(Device d) {
        value++;
        FileHandler.append("Connection " + d.getConnectionID() + ": " + d.getName() + " Logged out\n","output.txt");
        for (int i = 0; i < d.getRouter().getMaxConnections(); i++) {
            if (d.getRouter().getConnections()[i] == d) {
                d.getRouter().getConnections()[i] = null;
                break;
            }
        }
        if(value<=0) // if there is still waiting devices
            notify(); // notify one of them, which is the first one in the queue
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
        router.occupyConnection(this);
        FileHandler.append("Connection " + connectionID + ": " + name + " login\n","output.txt");
        try {
            Thread.sleep((long) (Math.random() * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        FileHandler.append("Connection " + connectionID + ": " + name + " performs online activity\n","output.txt");
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
