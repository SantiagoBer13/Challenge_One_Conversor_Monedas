package Models;

import java.util.ArrayList;
import java.util.List;

public class Persona {
    private String username;
    private String password;
    private List<Conversor> historial;

    public Persona(String username, String password) {
        this.username = username;
        this.password = password;
        this.historial = new ArrayList<>();
    }

    // Getters y Setters para username, password y historial
    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return password;
    }

    public List<Conversor> getHistorial() {
        return historial;
    }

    public void addConversion(Conversor conversion) {
        this.historial.add(conversion);
    }

    public void historialCoversiones(){
        if(this.historial.size() == 0){
            System.out.println("No hay conversiones en el historial.");
        }else{
            System.out.println("Historial de Conversiones: ");
            for(Conversor conversor: this.historial){
                System.out.println(conversor);
            }
            System.out.print("\n");
        }
    }


}
