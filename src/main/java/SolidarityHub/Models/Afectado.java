package SolidarityHub.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "afectados")
public class Afectado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String email;
    private String contrasena;
    private String direccion; // Añade un campo dirección

    public Afectado() {}

    public Afectado(String nombre, String email, String contrasena, String direccion) {
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.direccion = direccion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}