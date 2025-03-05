package SolidarityHub.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "voluntarios")
public class Voluntario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String email;
    private String contrasena;
    private String telefono; // Añade un campo teléfono
    //Getters y Setters
    private String areaInteres; // Area de interes del voluntario

    public Voluntario() {}

    public Voluntario(String nombre, String email, String contrasena, String telefono, String areaInteres) {
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.areaInteres = areaInteres;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getAreaInteres() { return areaInteres; }
    public void setAreaInteres(String areaInteres) { this.areaInteres = areaInteres; }
}