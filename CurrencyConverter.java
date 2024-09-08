import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scan;


public class CurrencyConverter {
    private static final String API_KEY = "YOUR_API_KEY"; // Replace with your API key
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Step 1: Get base currency and target currency from the user
        System.out.print("Enter the base currency (e.g., USD, EUR): ");
        String baseCurrency = scanner.nextLine().toUpperCase();
        System.out.print("Enter the target currency (e.g., USD, EUR): ");
        String targetCurrency = scanner.nextLine().toUpperCase();

        // Step 2: Get the amount to convert from the user
        System.out.print("Enter the amount to convert: ");
        double amount = scanner.nextDouble();

        // Step 3: Fetch the exchange rate
        double exchangeRate = fetchExchangeRate(baseCurrency, targetCurrency);

        if (exchangeRate == -1) {
            System.out.println("Failed to fetch exchange rate. Please try again.");
            return;
        }

        // Step 4: Perform the conversion
        double convertedAmount = amount * exchangeRate;

        // Step 5: Display the result
        System.out.printf("%.2f %s is equivalent to %.2f %s%n", amount, baseCurrency, convertedAmount, targetCurrency);

        scanner.close();
    }

    private static double fetchExchangeRate(String baseCurrency, String targetCurrency) {
        try {
            String url = API_URL + API_KEY + "/latest/" + baseCurrency;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject json = new JSONObject(response.body());

            if (json.getString("result").equals("success")) {
                JSONObject conversionRates = json.getJSONObject("conversion_rates");
                return conversionRates.getDouble(targetCurrency);
            } else {
                System.out.println("Error: " + json.getString("error-type"));
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
