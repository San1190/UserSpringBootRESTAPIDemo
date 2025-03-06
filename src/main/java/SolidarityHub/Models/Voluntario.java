// Voluntario.java
package SolidarityHub.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("voluntario")
public class Voluntario extends Usuario {
    public Voluntario() {}

    public Voluntario(String nombre, String email, String contrasena, byte[] foto) {
        super(nombre, email, contrasena, foto);
    }
     @Override
     public String getTipo_usuario() {
        return "voluntario"; // Asegúrate de que sea en minúsculas
    }
    @Override
    public String getTipo() {
        return "voluntario"; // Asegúrate de que sea en minúsculas
    }
}