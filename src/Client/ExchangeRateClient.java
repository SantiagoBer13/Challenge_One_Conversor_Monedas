package Client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ExchangeRateClient {
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/94f6611b3cd1544beda41de3/pair/";
    private final HttpClient client;

    // Constructor que inicializa el HttpClient
    public ExchangeRateClient() {
        this.client = HttpClient.newHttpClient();
    }

    // Método para hacer la solicitud con dos códigos de moneda
    public String getExchangeRate(String fromCurrency, String toCurrency, Double value) throws IOException, InterruptedException {
        String url = BASE_URL + fromCurrency + "/" + toCurrency + "/" + value;
        URI uri = URI.create(url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
