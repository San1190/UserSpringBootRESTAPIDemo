package SolidarityHub.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.component.dependency.NpmPackage;

@Route("login") // Ruta para acceder a la vista de login
@PageTitle("Login") // Título de la página
@NpmPackage(value = "@fontsource/montserrat", version = "4.5.0") // Dependencia para la fuente
public class LoginView extends VerticalLayout {

    private final LoginForm loginForm = new LoginForm();

    public LoginView() {
        // Configuración del layout de la página de login
        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        // Título de la página
        H1 header = new H1("Solidarity Hub - Login");
        header.addClassNames(LumoUtility.Margin.Vertical.XLARGE,
                LumoUtility.TextAlignment.CENTER, LumoUtility.FontSize.XXLARGE);

        // Configuración del formulario de login
        loginForm.setAction("login"); // La acción del formulario es "login"
        loginForm.setForgotPasswordButtonVisible(false); // No mostrar botón de "forgot password"

        // Agregar el formulario de login y el enlace de registro
        add(header);
        add(loginForm);

        // Enlace para redirigir a la página de registro
        RouterLink registerLink = new RouterLink("¿No tienes una cuenta? Regístrate aquí", RegistrationView.class);
        registerLink.addClassNames(LumoUtility.Margin.Top.SMALL, LumoUtility.TextAlignment.CENTER);
        add(registerLink);
    }
}
