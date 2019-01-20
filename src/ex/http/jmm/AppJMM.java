package ex.http.jmm;

public class AppJMM {
    static volatile boolean ready = false; //если закоментить volatile программа подвиснет
    static int data = 0;

    public static void main(String[] args) {
        new Thread(() -> {
            sleep(1000);
            data = 1;
            ready = true;
            data = 10;
        }).start();
        data = 2;
        new Thread(() -> {
            while (!ready) ;
            System.out.println(data);
        }).start();
    }
    private static void sleep(long timeout){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
