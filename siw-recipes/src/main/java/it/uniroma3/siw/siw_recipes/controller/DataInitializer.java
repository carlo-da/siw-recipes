package it.uniroma3.siw.siw_recipes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import it.uniroma3.siw.siw_recipes.model.User;
import it.uniroma3.siw.siw_recipes.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    // --- DEFINIZIONE UTENZE STATICHE ---
    private static final String ADMIN_EMAIL = "admin@siw.it";
    private static final String ADMIN_PASSWORD = "admin";
    
    private static final String USER_EMAIL = "user@siw.it";
    private static final String USER_PASSWORD = "password";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        
        // Crea (o aggiorna) l'ADMIN
        User admin = userRepository.findByEmail(ADMIN_EMAIL).orElse(new User());
        // Se non esiste, impostiamo i dati anagrafici
        if (admin.getId() == null) {
            admin.setName("Admin");
            admin.setSurname("Sistema");
            admin.setEmail(ADMIN_EMAIL);
            admin.setRole("ADMIN");
            admin.setEnabled(true);
            // La password la criptiamo SOLO in fase di creazione
            admin.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
            userRepository.save(admin);
            System.out.println(">>> CONFIGURAZIONE: Utente ADMIN garantito (" + ADMIN_EMAIL + ")");
        }

        // Crea (o aggiorna) lo USER
        User user = userRepository.findByEmail(USER_EMAIL).orElse(new User());
        if (user.getId() == null) {
            user.setName("Mario");
            user.setSurname("Rossi");
            user.setEmail(USER_EMAIL);
            user.setRole("DEFAULT");
            user.setEnabled(true);
            user.setPassword(passwordEncoder.encode(USER_PASSWORD));
            userRepository.save(user);
            System.out.println(">>> CONFIGURAZIONE: Utente USER garantito (" + USER_EMAIL + ")");
        }
    }
}
