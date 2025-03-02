package SolidarityHub;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.Key;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Route("")
@CssImport("./styles/styles.css") 
public class MainView extends VerticalLayout {

    private final Grid<Usuario> grid = new Grid<>(Usuario.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private final UsuarioRepositorio usuarioRepositorio;

    // Constructor
    public MainView(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;

        // Botón para cargar los usuarios
        Button cargarUsuarios = new Button("Cargar Usuarios", e -> cargarDatos());

        // Botón para registrar un nuevo usuario
        Button registrarUsuario = new Button("Registrar Usuario", e -> abrirFormularioRegistro());

        // Botón para borrar todos los usuarios
        Button borrarUsuarios = new Button("Borrar Todos los Usuarios", e -> borrarUsuarios());

        Button miBoton = new Button("Botón Estilizado");
        miBoton.getElement().getStyle().set("background-color", "#4CAF50");
        miBoton.getElement().getStyle().set("color", "white");
        miBoton.getElement().getStyle().set("padding", "10px 20px");

        add(cargarUsuarios, registrarUsuario, borrarUsuarios, miBoton, grid);
    }

    // Método para cargar los usuarios desde la API
    private void cargarDatos() {
        String url = "http://localhost:8081/api/usuarios";
        Usuario[] usuarios = restTemplate.getForObject(url, Usuario[].class);
        List<Usuario> listaUsuarios = Arrays.asList(usuarios);
        grid.setItems(listaUsuarios);
    }

    // Método para abrir el formulario de registro en un Pop-up
    private void abrirFormularioRegistro() {
        // Crear los campos del formulario
        TextField nombreField = new TextField("Nombre");
        TextField emailField = new TextField("Email");
        PasswordField contrasenaField = new PasswordField("Contraseña");

        // Crear el botón de guardar
        Button guardarButton = new Button("Guardar", e -> guardarUsuario(nombreField.getValue(), emailField.getValue(), contrasenaField.getValue()));

        // Crear un formulario en un FormLayout
        FormLayout formLayout = new FormLayout();
        formLayout.add(nombreField, emailField, contrasenaField, guardarButton);

        // Crear el diálogo (pop-up)
        Dialog dialog = new Dialog();
        dialog.add(formLayout);
        dialog.setWidth("300px");
        dialog.setHeight("250px");

        // Detectar presionar Enter en el campo de contraseña o nombre
        nombreField.addKeyDownListener(e -> {
            if (Key.ENTER.equals(e.getKey())) {
                guardarUsuario(nombreField.getValue(), emailField.getValue(), contrasenaField.getValue());
                dialog.close(); // Cerrar el pop-up después de guardar
            }
        });

        emailField.addKeyDownListener(e -> {
            if (Key.ENTER.equals(e.getKey())) {
                guardarUsuario(nombreField.getValue(), emailField.getValue(), contrasenaField.getValue());
                dialog.close(); // Cerrar el pop-up después de guardar
            }
        });

        contrasenaField.addKeyDownListener(e -> {
            if (Key.ENTER.equals(e.getKey())) {
                guardarUsuario(nombreField.getValue(), emailField.getValue(), contrasenaField.getValue());
                dialog.close(); // Cerrar el pop-up después de guardar
            }
        });

        // Mostrar el diálogo
        dialog.open();
    }

    // Método para guardar el usuario
    private void guardarUsuario(String nombre, String email, String contrasena) {
        // Crear un nuevo objeto Usuario con los datos del formulario
        Usuario nuevoUsuario = new Usuario(nombre, email, contrasena);

        // Guardar el nuevo usuario en la base de datos
        try {
            usuarioRepositorio.save(nuevoUsuario);
            Notification.show("Usuario registrado correctamente", 3000, Notification.Position.MIDDLE);
            cargarDatos(); // Recargar los datos (esto actualizará la grid)
        } catch (Exception e) {
            Notification.show("Error al registrar el usuario: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
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
