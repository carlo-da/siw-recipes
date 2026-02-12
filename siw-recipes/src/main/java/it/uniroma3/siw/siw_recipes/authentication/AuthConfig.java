package it.uniroma3.siw.siw_recipes.authentication;

import it.uniroma3.siw.siw_recipes.authentication.OAuth2LoginSuccessHandler;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class AuthConfig {

    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Autowired
    private DataSource dataSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                // Risorse statiche e Pagine Pubbliche
                .requestMatchers("/css/**", "/images/**", "/js/**").permitAll()
                .requestMatchers("/", "/index", "/register", "/login", "/about").permitAll()
                .requestMatchers(HttpMethod.GET, "/recipe/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/recipe-images/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/category/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/search").permitAll()
                .requestMatchers(HttpMethod.GET, "/search").permitAll()
                // Area Admin
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                // Tutto il resto richiede login
                .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
                )
                .oauth2Login((oauth2) -> oauth2
                .loginPage("/login") // Usa la tua pagina di login personalizzata
                .successHandler(oAuth2LoginSuccessHandler) // Usa il gestore per salvare l'utente nel DB
                )
                .logout((logout) -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
