import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.concurrent.TimeUnit;


public class Main {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static volatile int threadCount = 0;
    private static final int threadLimit = 2;
    private static long startingPoint = 0;

    public static void main(String[] args) {
        Runnable task = () -> {
            Product[] products = new Product[]{new Product(
                    "string",
                    new Date(),
                    "string",
                    "string",
                    "string",
                    new Date(),
                    "string",
                    "string",
                    "string"
            )};

            Doc doc = new Doc("string",
                    "string",
                    "LP_INTRODUCE_GOODS",
                    true,
                    "string",
                    "string",
                    "string",
                    new Date(),
                    "string",
                    products,
                    new Date(),
                    "string"
            );

            CrptApi crptApi = new CrptApi(TimeUnit.MINUTES, 5);
            crptApi.uploadDoc(doc, "string");
        };

        for (int i = 0; i < 20; i++) {
            new Thread(task).start();
        }

    }




















//    public static void main(String[] args) {
//        Runnable task = () -> {
//            try {
//                Main main = new Main();
//                main.limitedMethod();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        };
//
//        new Thread(task).start();
//        new Thread(task).start();
//        new Thread(task).start();
//        new Thread(task).start();
//        new Thread(task).start();
//
//
//
//
//    }
//
//
//    public synchronized void limitedMethod() throws InterruptedException {
//        if (startingPoint == 0) {
//            startingPoint = System.currentTimeMillis();
//        }
//        //60000
//        int timeUnitInMills = 10000;
//
//        if (threadCount >= threadLimit) {
//            while (true) {
//                if (System.currentTimeMillis() > startingPoint + timeUnitInMills) {
//                    startingPoint = System.currentTimeMillis();
//                    threadCount = 0;
//                    break;
//                } else {
//                    Thread.sleep(timeUnitInMills / 100);        // Чтобы ноут не грелся
//                }
//            }
//        }
//
//        threadCount++;
//
//        for (int i = 1; i <= 3; i++) {
//            Thread.sleep(500);
//            System.out.println(i);
//        }
//        System.out.println(Thread.currentThread().getName() + " completed the task");
//    }
}
