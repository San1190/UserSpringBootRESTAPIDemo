package SolidarityHub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/login", "/register", "/css/**", "/js/**", "/images/**").permitAll() // Rutas públicas
                        .requestMatchers("/main").authenticated() // Necesita autenticación para acceder a /main
                        .anyRequest().permitAll() // Permite cualquier otra petición
                )
                .formLogin((form) -> form
                        .loginPage("/login") // Página de login personalizada
                        .permitAll() // Permite acceder a la página de login
                        .defaultSuccessUrl("/main", true) // Redirige a /main después del login exitoso
                )
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout") // Redirige a login tras cerrar sesión
                        .permitAll()
                );
        return http.build();
    }
}
