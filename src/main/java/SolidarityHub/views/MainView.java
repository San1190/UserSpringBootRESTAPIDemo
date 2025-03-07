package SolidarityHub.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import SolidarityHub.models.Usuario;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Route("")
public class MainView extends VerticalLayout {

    private final Grid<Usuario> grid = new Grid<>(Usuario.class);
    private final RestTemplate restTemplate = new RestTemplate();

    public MainView() {
        setSizeFull();
        configurarGrid();

        Button cargarUsuarios = new Button("Cargar Usuarios", e -> cargarDatos());
        Button registrarUsuario = new Button("Registrar Usuario", e -> abrirFormularioRegistro());

        add(new HorizontalLayout(cargarUsuarios, registrarUsuario), grid);

        cargarDatos(); // Cargar usuarios al iniciar
    }

    private void configurarGrid() {
        grid.setSizeFull();
        grid.removeAllColumns();  // **Añadir esta línea para eliminar las columnas predeterminadas**
    
        // Define las columnas que quieres mostrar y el orden
        grid.addColumn(Usuario::getId).setHeader("ID").setSortable(true);
        grid.addColumn(Usuario::getNombre).setHeader("Nombre").setSortable(true);
        grid.addColumn(Usuario::getEmail).setHeader("Email").setSortable(true);
        grid.addColumn(usuario -> usuario.getTipo()).setHeader("Tipo").setSortable(true);
    
        // Formatear la columna de la foto (si decides mostrarla - requiere convertir byte[] a imagen)
        grid.addComponentColumn(usuario -> {
            if (usuario.getFoto() != null) {
                StreamResource resource = new StreamResource("foto", () -> new ByteArrayInputStream(usuario.getFoto()));
                Image image = new Image(resource, "Foto de " + usuario.getNombre());
                image.setWidth("50px");
                image.setHeight("50px");
                return image;
            } else {
                return new Label("Sin foto");
            }
        }).setHeader("Foto");
    }

    private void cargarDatos() {
        try {
            String url = "http://localhost:8081/api/usuarios";
            Usuario[] usuarios = restTemplate.getForObject(url, Usuario[].class);
            List<Usuario> listaUsuarios = Arrays.asList(usuarios);
            grid.setItems(listaUsuarios);
        } catch (Exception e) {
            e.printStackTrace();  // Imprime el error completo en la consola
            Notification.show("Error al cargar los usuarios: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private void abrirFormularioRegistro() {
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");
        dialog.setHeight("500px");

        TextField nombreField = new TextField("Nombre");
        EmailField emailField = new EmailField("Email");
        PasswordField contrasenaField = new PasswordField("Contraseña");

        // Componente Upload
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");  // Tipos de archivo permitidos
        Image previewImage = new Image();
        previewImage.setWidth("100px");
        previewImage.setHeight("100px");
        previewImage.setVisible(false); // Inicialmente oculta

        upload.addSucceededListener(event -> {
            InputStream inputStream = buffer.getInputStream();
            try {
                byte[] bytes = inputStream.readAllBytes();
                StreamResource resource = new StreamResource(event.getFileName(), () -> new ByteArrayInputStream(bytes));
                previewImage.setSrc(resource);
                previewImage.setVisible(true);
            } catch (IOException e) {
                Notification.show("Error al leer la imagen", 3000, Notification.Position.MIDDLE);
                previewImage.setVisible(false);
            }
        });

        upload.addFailedListener(event -> {
            Notification.show("Error al subir la imagen", 3000, Notification.Position.MIDDLE);
            previewImage.setVisible(false);
        });

        upload.addStartedListener(event -> previewImage.setVisible(false));

        Button afectadoButton = new Button("Afectado");
        Button voluntarioButton = new Button("Voluntario");
        Button cancelarButton = new Button("Cancelar", e -> dialog.close());

        afectadoButton.addClickListener(e -> {
            InputStream inputStream = buffer.getInputStream();
            byte[] fotoBytes = null;
            if (inputStream != null) {
                try {
                    fotoBytes = inputStream.readAllBytes();
                } catch (IOException ex) {
                    Notification.show("Error al leer la imagen", 3000, Notification.Position.MIDDLE);
                    fotoBytes = null;
                }
            }
            registrarUsuario(dialog, "afectado", nombreField, emailField, contrasenaField, fotoBytes);
        });

        voluntarioButton.addClickListener(e -> {
            InputStream inputStream = buffer.getInputStream();
            byte[] fotoBytes = null;
            if (inputStream != null) {
                try {
                    fotoBytes = inputStream.readAllBytes();
                } catch (IOException ex) {
                    Notification.show("Error al leer la imagen", 3000, Notification.Position.MIDDLE);
                    fotoBytes = null;
                }
            }
            registrarUsuario(dialog, "voluntario", nombreField, emailField, contrasenaField, fotoBytes);
        });

        HorizontalLayout tipoLayout = new HorizontalLayout(afectadoButton, voluntarioButton);
        tipoLayout.setSpacing(true);

        VerticalLayout layout = new VerticalLayout(nombreField, emailField, contrasenaField, upload, previewImage, tipoLayout, cancelarButton);
        layout.setSpacing(true);
        dialog.add(layout);
        dialog.open();
    }

     private void registrarUsuario(Dialog dialog, String tipo, TextField nombreField, EmailField emailField, PasswordField contrasenaField, byte[] fotoBytes) {
        String nombre = nombreField.getValue();
        String email = emailField.getValue();
        String contrasena = contrasenaField.getValue();

        if (nombre.isEmpty() || email.isEmpty()) {
            Notification.show("Por favor, complete todos los campos", 3000, Notification.Position.MIDDLE);
            return;
        }

        try {
            String url = "http://localhost:8081/api/usuarios/registrar";

            // Configurar los encabezados para la petición multipart/form-data
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // Crear el cuerpo de la petición como un MultiValueMap
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("tipo", tipo);
            body.add("nombre", nombre);
            body.add("email", email);
            body.add("contrasena", contrasena);

            // Agregar la foto al cuerpo de la petición
            if (fotoBytes != null && fotoBytes.length > 0) {
                ByteArrayResource resource = new ByteArrayResource(fotoBytes) {
                    @Override
                    public String getFilename() {
                        return "foto.jpg"; // Nombre del archivo
                    }
                };
                body.add("foto", resource);
            }

            // Crear la petición HttpEntity con los encabezados y el cuerpo
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Realizar la petición POST
            ResponseEntity<Usuario> response = restTemplate.postForEntity(url, requestEntity, Usuario.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                Notification.show("Usuario registrado correctamente");
                System.out.println(response);
                cargarDatos();
                dialog.close();
            } else {
                Notification.show("Error al registrar usuario: " + response.getStatusCode());
            }

        } catch (Exception e) {
            Notification.show("Error al registrar usuario: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }
}