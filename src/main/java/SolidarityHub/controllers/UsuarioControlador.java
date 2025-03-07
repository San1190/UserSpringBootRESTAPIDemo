package SolidarityHub.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import SolidarityHub.models.Usuario;
import SolidarityHub.services.UsuarioService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioControlador {

    private final UsuarioService usuarioService;

    public UsuarioControlador(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> obtenerUsuarios() {
        try {
            return usuarioService.obtenerUsuarios();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener los usuarios", e);
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<Usuario> registrarUsuario(
            @RequestParam String tipo,
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String contrasena,
            @RequestParam(required = false) MultipartFile foto) {

        byte[] fotoBytes = null;

        // Verificar si el archivo de foto fue proporcionado y no está vacío
        if (foto != null && !foto.isEmpty()) {
            try {
                fotoBytes = foto.getBytes(); // Convertir el archivo MultipartFile a un array de bytes
            } catch (IOException e) {
                // Si hay un error al leer la foto, devuelve un error 500 (Internal Server Error) con un mensaje
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(null); // O podrías devolver un Usuario con un mensaje de error
            }
        }

        try {
            // Llama al servicio para registrar el usuario, pasando el array de bytes de la foto
            Usuario nuevoUsuario = usuarioService.registrarUsuario(tipo, nombre, email, contrasena, fotoBytes);

            // Si el registro es exitoso, devuelve una respuesta 201 (Created) con el objeto Usuario creado
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (Exception e) {
            // Si hay un error durante el registro, devuelve un error 500 con un mensaje
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // O podrías devolver un Usuario con un mensaje de error
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarUsuario(@PathVariable Long id) {
        try {
            usuarioService.borrarUsuario(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }
}