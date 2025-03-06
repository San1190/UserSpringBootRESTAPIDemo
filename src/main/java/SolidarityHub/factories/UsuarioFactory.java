// UsuarioFactory.java
package SolidarityHub.factories;

import SolidarityHub.models.Afectado;
import SolidarityHub.models.Voluntario;
import SolidarityHub.models.Usuario;

public class UsuarioFactory {

    public static Usuario crearUsuario(String tipo, String nombre, String email, String contrasena, byte[] foto) {
        switch (tipo.toLowerCase()) {
            case "afectado":
                return new Afectado(nombre, email, contrasena, foto);
            case "voluntario":
                return new Voluntario(nombre, email, contrasena, foto);
            default:
                throw new IllegalArgumentException("Tipo de usuario no válido: " + tipo);
        }
    }
}