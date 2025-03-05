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
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.IOException;

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




        add(cargarUsuarios, registrarUsuario, borrarUsuarios, grid);
    }

    // Método para cargar los usuarios desde la API
    private void cargarDatos() {
        String url = "http://localhost:8081/api/usuarios";
        Usuario[] usuarios = restTemplate.getForObject(url, Usuario[].class);
        List<Usuario> listaUsuarios = Arrays.asList(usuarios);
        //Filtrar lista usduarios para quitar el ultimo elemento de cada usuario
        System.out.println("Lista de usuarios: " + listaUsuarios);
                
        // Mostrar la foto en la grid
        grid.setItems(listaUsuarios);
        grid.addColumn(new ComponentRenderer<>(usuario -> {
            if (usuario.getFoto() != null) {
                String base64Image = "data:image/png;base64," + Base64.getEncoder().encodeToString(usuario.getFoto());
                Image image = new Image(base64Image, "Foto de " + usuario.getNombre());
                image.setWidth("100px");
                return image;
            }
            return null;
        })).setHeader("Foto").setWidth("120px");
    }

    // Método para abrir el formulario de registro en un Pop-up
    private void abrirFormularioRegistro() {
        // Crear los campos del formulario
        TextField nombreField = new TextField("Nombre");
        TextField emailField = new TextField("Email");
        PasswordField contrasenaField = new PasswordField("Contraseña");

        // Configurar el buffer de memoria para cargar la imagen
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("image/jpeg", "image/png");
        upload.addSucceededListener(event -> {
            try {
                byte[] fotoBytes = buffer.getInputStream().readAllBytes();
                Notification.show("Imagen cargada correctamente", 3000, Notification.Position.MIDDLE);
            } catch (IOException ex) {
                Notification.show("Error al cargar la imagen: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        // Botón para guardar el usuario
        Button guardarButton = new Button("Guardar", e -> guardarUsuario(nombreField.getValue(), emailField.getValue(), contrasenaField.getValue(), buffer));

        // Crear un formulario en un FormLayout
        FormLayout formLayout = new FormLayout();
        formLayout.add(nombreField, emailField, contrasenaField, upload, guardarButton);

        // Crear el diálogo (pop-up)
        Dialog dialog = new Dialog();
        dialog.add(formLayout);
        dialog.setWidth("300px");
        dialog.setHeight("350px");
        dialog.open();
    }

    // Método para guardar el usuario
    private void guardarUsuario(String nombre, String email, String contrasena, MemoryBuffer buffer) {
        byte[] fotoBytes;
        try {
            fotoBytes = buffer.getInputStream().readAllBytes();
        } catch (IOException e) {
            Notification.show("Error al leer la imagen: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            return;
        }
        Usuario nuevoUsuario = new Usuario(nombre, email, contrasena, fotoBytes);

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
