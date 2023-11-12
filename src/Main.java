
public class Main {
    public static void main(String[] args) {
        Router router = new Router(2);
        Device d1 = new Device(router, 1);
        Device d2 = new Device(router, 2);
        Device d3 = new Device(router, 3);
        Device d4 = new Device(router, 4);
        d1.start();
        d2.start();
        d3.start();
        d4.start();

    }
}