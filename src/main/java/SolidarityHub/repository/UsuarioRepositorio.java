package SolidarityHub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import SolidarityHub.models.Usuario;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);  // Método para buscar un usuario por su email
}
