import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CrptApi {
    private final TimeUnit timeUnit;
    private final int requestLimit;
    private volatile AtomicInteger threadCount = new AtomicInteger(0);
    private long startingPoint = 0;
    private static final ObjectMapper objectMapper = getObjectMapper();

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.timeUnit = timeUnit;
        this.requestLimit = requestLimit;
    }

    public synchronized void uploadDoc(Doc doc, String description) {
        try {
            RpsLimiter();

            /* Не понял, что подразумевалось под подписью в задании, если речь шла о подписи как о Signature,
            то я не знаю как это реализовать без сторонних библиотек */
            doc.setDescription(new Description(description));

            String payload = toJson(doc);
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8080/test"))
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void RpsLimiter() throws InterruptedException {
        if (startingPoint == 0) {
            startingPoint = System.currentTimeMillis();
        }

        long timeUnitToMills = timeUnit.toMillis(1);

        if (threadCount.get() == requestLimit) {
            while (true) {
                if (System.currentTimeMillis() > startingPoint + timeUnitToMills) {
                    startingPoint = System.currentTimeMillis();
                    threadCount = new AtomicInteger(0);
                    break;
                } else {
                    Thread.sleep(timeUnitToMills / 100);    // Чтобы не итерироваться каждую милисекунду
                }
            }
        }
        threadCount.incrementAndGet();
    }

    private static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        objectMapper.setDateFormat(df);

        return objectMapper;
    }

    public String toJson(Doc doc) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.valueToTree(doc);
        ObjectWriter objectWriter = objectMapper.writer();
        return objectWriter.writeValueAsString(jsonNode);
    }
}

class Doc {
    Description description;
    String id;
    String status;
    String type;
    boolean importRequest;
    String ownerInn;
    String participantInn;
    String producerInn;
    Date productionDate;
    String productionType;
    Product[] products;
    Date regDate;
    String regNumber;

    public Doc(String id,
               String status,
               String type,
               boolean importRequest,
               String ownerInn,
               String participantInn,
               String producerInn,
               Date productionDate,
               String productionType,
               Product[] products,
               Date regDate,
               String regNumber) {
        this.id = id;
        this.status = status;
        this.type = type;
        this.importRequest = importRequest;
        this.ownerInn = ownerInn;
        this.participantInn = participantInn;
        this.producerInn = producerInn;
        this.productionDate = productionDate;
        this.productionType = productionType;
        this.products = products;
        this.regDate = regDate;
        this.regNumber = regNumber;
    }

    public void setDescription(Description description) {
        this.description = description;
    }
}

class Product {
    String certificateDocument;
    Date certificateDocumentDate;
    String certificateDocumentNumber;
    String ownerInn;
    String producerInn;
    Date productionDate;
    String tnvedCode;
    String uitCode;
    String uituCode;

    public Product(String certificateDocument,
                   Date certificateDocumentDate,
                   String certificateDocumentNumber,
                   String ownerInn,
                   String producerInn,
                   Date productionDate,
                   String tnvedCode,
                   String uitCode,
                   String uituCode) {
        this.certificateDocument = certificateDocument;
        this.certificateDocumentDate = certificateDocumentDate;
        this.certificateDocumentNumber = certificateDocumentNumber;
        this.ownerInn = ownerInn;
        this.producerInn = producerInn;
        this.productionDate = productionDate;
        this.tnvedCode = tnvedCode;
        this.uitCode = uitCode;
        this.uituCode = uituCode;
    }
}

class Description {
    String participantInn;

    public Description(String participantInn) {
        this.participantInn = participantInn;
    }
}