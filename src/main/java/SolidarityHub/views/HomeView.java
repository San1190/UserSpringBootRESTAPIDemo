package SolidarityHub.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;

import jakarta.annotation.security.PermitAll;

@Route("home")
@PermitAll
public class HomeView extends VerticalLayout {

    public HomeView() {
        // Obtén el nombre del usuario de la sesión (si está disponible)
        String username = VaadinSession.getCurrent().getAttribute("user.name") != null ?
                          VaadinSession.getCurrent().getAttribute("user.name").toString() :
                          "Usuario"; // Valor por defecto si no hay nombre de usuario

        H1 title = new H1("Bienvenido, " + username + "!");
        add(title);

        // Agrega un botón de logout
        Button logoutButton = new Button("Logout", e -> {
            UI.getCurrent().getPage().setLocation("/logout"); // Redirige al endpoint de logout
        });
        add(logoutButton);

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }
}