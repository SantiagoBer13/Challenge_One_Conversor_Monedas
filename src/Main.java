import Client.ExchangeRateClient;
import Models.Conversor;
import Records.ConversorDTO;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        ExchangeRateClient exchangeRateClient = new ExchangeRateClient();
        Gson gson = new GsonBuilder()
                .create();

        System.out.println("**************************************");
        System.out.println("Bienvenido/a al Conversor de Moneda");

        while (true) {
            System.out.println("""
                Elige una opción:
                1) Dólar => Peso Argentino
                2) Peso Argentino => Dólar
                3) Dólar => Real Brasileño
                4) Real Brasileño => Dólar
                5) Dólar => Peso Colombiano
                6) Peso Colombiano => Dólar
                7) Salir
                **************************************
                """);

            int opcion;
            try {
                opcion = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida. Por favor ingresa un número.");
                continue;
            }

            String monedaBase = "";
            String monedaCambiada = "";

            switch (opcion) {
                case 1 -> {
                    monedaBase = "USD";
                    monedaCambiada = "ARS";
                }
                case 2 -> {
                    monedaBase = "ARS";
                    monedaCambiada = "USD";
                }
                case 3 -> {
                    monedaBase = "USD";
                    monedaCambiada = "BRL";
                }
                case 4 -> {
                    monedaBase = "BRL";
                    monedaCambiada = "USD";
                }
                case 5 -> {
                    monedaBase = "USD";
                    monedaCambiada = "COP";
                }
                case 6 -> {
                    monedaBase = "COP";
                    monedaCambiada = "USD";
                }
                case 7 -> {
                    System.out.println("Gracias por usar el conversor. ¡Hasta luego!");
                    return;
                }
                default -> {
                    System.out.println("Opción no válida. Inténtalo de nuevo.");
                    continue;
                }
            }

            double valor;
            while (true) {
                System.out.println("Ingresa el valor que deseas convertir: ");
                try {
                    valor = Double.parseDouble(input.nextLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Valor inválido. Por favor ingresa un número.\n");
                }
            }

            try {
                String json = exchangeRateClient.getExchangeRate(monedaBase, monedaCambiada, valor);
                ConversorDTO conversorDto = gson.fromJson(json, ConversorDTO.class);
                Conversor converted = new Conversor(conversorDto, valor);
                System.out.println(converted);
            } catch (IOException | InterruptedException e) {
                System.out.println("Error al obtener la tasa de cambio: " + e.getMessage());
            }
        }
    }
}