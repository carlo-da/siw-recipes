
package it.uniroma3.siw.siw_recipes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import it.uniroma3.siw.siw_recipes.model.User;
import it.uniroma3.siw.siw_recipes.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    // --- DEFINIZIONE COSTANTI UTENTI ---
    private static final String ADMIN_EMAIL = "admin@siw.it";
    private static final String ADMIN_PASS = "admin";

    private static final String MARIO_EMAIL = "mario@siw.it";
    private static final String MARIO_PASS = "password";

    private static final String LUIGI_EMAIL = "luigi@siw.it";
    private static final String LUIGI_PASS = "password";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        
        System.out.println(">>> DATA INITIALIZER: Controllo utenze...");

        // 1. ADMIN
        ensureUser("Admin", "Sistema", ADMIN_EMAIL, ADMIN_PASS, "ADMIN");

        // 2. MARIO ROSSI (Utente standard)
        ensureUser("Mario", "Rossi", MARIO_EMAIL, MARIO_PASS, "DEFAULT");

        // 3. LUIGI VERDI (Utente standard)
        ensureUser("Luigi", "Verdi", LUIGI_EMAIL, LUIGI_PASS, "DEFAULT");
        
        System.out.println(">>> DATA INITIALIZER: Fatto. Utenti pronti.");
    }

    private void ensureUser(String name, String surname, String email, String rawPassword, String role) {
        User user = userRepository.findByEmail(email).orElse(new User());
        
        // Se l'ID è null, significa che l'utente non è nel DB
        if (user.getId() == null) {
            user.setName(name);
            user.setSurname(surname);
            user.setEmail(email);
            user.setRole(role);
            user.setEnabled(true);
            // Criptiamo la password
            user.setPassword(passwordEncoder.encode(rawPassword));
            
            userRepository.save(user);
            System.out.println(">>> CREATO UTENTE: " + name + " (" + email + ")");
        } else {
            System.out.println(">>> UTENTE ESISTENTE: " + email);
        }
    }
}