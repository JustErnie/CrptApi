import java.util.Date;
import java.util.concurrent.TimeUnit;


public class Main {
    public static void main(String[] args) {
        CrptApi crptApi = new CrptApi(TimeUnit.SECONDS, 3);
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
            crptApi.uploadDoc(doc, "string");
        };

        for (int i = 0; i < 20; i++) {
            new Thread(task).start();
        }

    }
}
