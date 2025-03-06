package SolidarityHub.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import SolidarityHub.models.Usuario;
import SolidarityHub.repository.UsuarioRepositorio;

@Route("register")
@PageTitle("Register")
public class RegistrationView extends VerticalLayout {

    private final UsuarioRepositorio userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationView(UsuarioRepositorio userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

        TextField usernameField = new TextField("Username");
        PasswordField passwordField = new PasswordField("Password");
        Button registerButton = new Button("Register", event -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();

            // Check if username is already in use
            if (userRepository.findByEmail(username) != null) {
                Notification.show("Username already taken.").addThemeName("error");
                return;
            }

            // Encode the password
            String encodedPassword = passwordEncoder.encode(password);

            // Create a new user
            Usuario newUser = new Usuario(username, username, encodedPassword, null) {
                @Override
                public String getTipo() {
                    return "user";
                }

                @Override
                public String getTipo_usuario() {
                    return null;
                }
            };

            // Save the new user
            userRepository.save(newUser);

            Notification.show("Registration successful.").addThemeName("success");
        });

        add(
                usernameField,
                passwordField,
                registerButton,
                new RouterLink("Login", LoginView.class)
        );

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }
}