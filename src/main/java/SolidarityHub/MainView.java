package SolidarityHub;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

@Route("")
public class MainView extends VerticalLayout {

    private final Grid<Usuario> grid = new Grid<>(Usuario.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private final UsuarioRepositorio usuarioRepositorio;

    public MainView(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;

        // Botón para cargar los usuarios
        Button cargarUsuarios = new Button("Cargar Usuarios", e -> cargarDatos());

        // Botón para registrar un nuevo usuario
        Button registrarUsuario = new Button("Registrar Usuario", e -> abrirFormularioRegistro());

        // Botón para borrar todos los usuarios
        Button borrarUsuarios = new Button("Borrar Todos los Usuarios", e -> borrarUsuarios());

        add(cargarUsuarios, registrarUsuario, borrarUsuarios, grid);
    }

    // Método para cargar los usuarios desde la API
    private void cargarDatos() {
        String url = "http://localhost:8081/api/usuarios";
        Usuario[] usuarios = restTemplate.getForObject(url, Usuario[].class);
        List<Usuario> listaUsuarios = Arrays.asList(usuarios);
        grid.setItems(listaUsuarios);
    }

    // Método para abrir el formulario de registro
    private void abrirFormularioRegistro() {
        // Aquí iría el formulario de registro
    }

    // Método para borrar todos los usuarios
    private void borrarUsuarios() {
        try {
            usuarioRepositorio.deleteAll();  // Borrar todos los usuarios de la base de datos
            cargarDatos(); // Recargar los datos (esto dejará la grid vacía)
            Notification.show("Todos los usuarios han sido eliminados", 3000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            Notification.show("Error al eliminar los usuarios: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }
}
