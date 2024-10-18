import Client.ExchangeRateClient;
import Models.Conversor;
import Models.Persona;
import Records.ConversorDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        List<Persona> usuarios = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        ExchangeRateClient exchangeRateClient = new ExchangeRateClient();
        Gson gson = new GsonBuilder().create();

        System.out.println("**************************************");
        System.out.println("Bienvenido/a al Conversor de Moneda");
        menu(usuarios, input, exchangeRateClient, gson);
        System.out.print("**************************************");
    }

    public static void menu(List<Persona> usuarios, Scanner input, ExchangeRateClient exchangeRateClient, Gson gson) {
        int opcion = 0;
        Persona usuarioActual = null;

        do {
            System.out.print("""
            1) Crear usuario
            2) Iniciar sesión
            3) Conversión rápida (sin cuenta)
            4) Salir
            """);
            try {
                opcion = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida. Por favor ingresa un número.");
                continue;
            }

            switch (opcion) {
                case 1 -> crearUsuario(usuarios, input);
                case 2 -> usuarioActual = iniciarSesion(usuarios, input);
                case 3 -> {
                    System.out.println("Conversión rápida (sin historial).");
                    Conversor conversion = realizarConversion(null, exchangeRateClient, gson, input);
                    if (conversion != null) {
                        System.out.println("Resultado: " + conversion);
                    }
                }
                case 4 -> System.out.println("Saliendo del programa...");
                default -> System.out.println("Opción no válida.");
            }

            // Si el usuario ha iniciado sesión
            if (usuarioActual != null) {
                System.out.println("Bienvenido, " + usuarioActual.getUsername());
                while (true) {
                    Conversor conversion = realizarConversion(usuarioActual, exchangeRateClient, gson, input);
                    if (conversion != null) {
                        usuarioActual.getHistorial().add(conversion); // Agregar al historial del usuario
                        System.out.println("Resultado: " + conversion + "\n");
                    } else {
                        break; // Si elige salir de la conversión
                    }
                }
                usuarioActual = null; // Cerrar sesión automáticamente
            }

        } while (opcion != 4);
    }

    public static Persona crearUsuario(List<Persona> usuarios, Scanner input) {
        System.out.print("Crea un nombre de usuario: ");
        String username = input.nextLine();
        System.out.print("Crea una contraseña: ");
        String password = input.nextLine();

        // Verificar si el usuario ya existe
        for (Persona usuario : usuarios) {
            if (usuario.getUsername().equals(username)) {
                System.out.println("El nombre de usuario ya existe.");
                return usuario;
            }
        }

        // Si no existe, lo agregamos a la lista
        Persona usuario = new Persona(username, password);
        usuarios.add(usuario);
        System.out.println("Usuario creado exitosamente. Inicia Sesiòn.\n");
        return usuario;
    }

    public static Persona iniciarSesion(List<Persona> usuarios, Scanner input) {
        System.out.print("Nombre de usuario: ");
        String username = input.nextLine();
        System.out.print("Contraseña: ");
        String password = input.nextLine();

        for (Persona usuario : usuarios) {
            if (usuario.getUsername().equals(username) && usuario.getPassword().equals(password)) {
                System.out.println("\nInicio de sesión exitoso.");
                return usuario; // Devuelve el usuario autenticado
            }
        }

        System.out.println("Usuario o contraseña incorrectos.");
        return null; // Si no se encuentra el usuario o la contraseña es incorrecta
    }

    public static Conversor realizarConversion(Persona usuario, ExchangeRateClient exchangeRateClient, Gson gson, Scanner input) {
        int opcion;
        String monedaBase = "";
        String monedaCambiada = "";

        while (true) {
            if(usuario != null){
                System.out.print("""
            Elige una opción:
            1) Dólar => Peso Argentino
            2) Peso Argentino => Dólar
            3) Dólar => Real Brasileño
            4) Real Brasileño => Dólar
            5) Dólar => Peso Colombiano
            6) Peso Colombiano => Dólar
            7) Ver historial de conversiones
            8) Volver
            """);
            }else{
                System.out.println("""
            Elige una opción:
            1) Dólar => Peso Argentino
            2) Peso Argentino => Dólar
            3) Dólar => Real Brasileño
            4) Real Brasileño => Dólar
            5) Dólar => Peso Colombiano
            6) Peso Colombiano => Dólar
            8) Volver
            """);
            }

            try {
                opcion = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida. Por favor ingresa un número.");
                continue;
            }

            if(usuario == null && opcion == 7){
                System.out.println("Opción no válida. Inténtalo de nuevo.");
                continue;
            }

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
                    usuario.historialCoversiones();
                    continue;
                }
                case 8 -> {
                    System.out.println("Volviendo al menú...");
                    return null;
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
                    System.out.println("Valor inválido. Por favor ingresa un número.");
                }
            }

            try {
                String json = exchangeRateClient.getExchangeRate(monedaBase, monedaCambiada, valor);
                ConversorDTO conversorDto = gson.fromJson(json, ConversorDTO.class);
                return new Conversor(conversorDto, valor);
            } catch (IOException | InterruptedException e) {
                System.out.println("Error al obtener la tasa de cambio: " + e.getMessage());
            }
        }
    }
}
