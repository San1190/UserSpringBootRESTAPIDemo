package SolidarityHub.services;

import SolidarityHub.factories.UsuarioFactory;
import SolidarityHub.models.Usuario;
import SolidarityHub.repository.UsuarioRepositorio;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepositorio usuarioRepositorio;

    public UsuarioService(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public List<Usuario> obtenerUsuarios() {
        return usuarioRepositorio.findAll();
    }

    public Usuario registrarUsuario(String tipo, String nombre, String email, String contrasena, byte[] foto) {
        Usuario nuevoUsuario = UsuarioFactory.crearUsuario(tipo, nombre, email, contrasena, foto);
        return usuarioRepositorio.save(nuevoUsuario);
    }
}
