package SolidarityHub;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepositorio usuarioRepositorio;

    public DataLoader(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Override
    public void run(String... args) throws Exception {
        // Verificar si el usuario ya existe antes de guardarlo
        if (usuarioRepositorio.findByEmail("juan.perez@email.com") == null) {
            usuarioRepositorio.save(new Usuario("Juan Pérez", "juan.perez@email.com", "contraseña123"));
        }
        if (usuarioRepositorio.findByEmail("ana.gomez@email.com") == null) {
            usuarioRepositorio.save(new Usuario("Ana Gómez", "ana.gomez@email.com", "contraseña456"));
        }
        if (usuarioRepositorio.findByEmail("carlos.lopez@email.com") == null) {
            usuarioRepositorio.save(new Usuario("Carlos López", "carlos.lopez@email.com", "contraseña789"));
        }
    }
}
