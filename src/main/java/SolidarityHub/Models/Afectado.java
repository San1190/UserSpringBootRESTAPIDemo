package SolidarityHub.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("afectado")
public class Afectado extends Usuario {
    public Afectado() {}
    public Afectado(String nombre, String email, String contrasena, byte[] foto) {
        super(nombre, email, contrasena, foto);
    }

    @Override
    public String getTipo_usuario() {
        return "afectado"; // Asegúrate de que sea en minúsculas
    }
    @Override
    public String getTipo() {
        return "afectado"; // Asegúrate de que sea en minúsculas
    }
}