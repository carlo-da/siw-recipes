package it.uniroma3.siw.siw_recipes.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.siw_recipes.model.User;
import it.uniroma3.siw.siw_recipes.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Cerchiamo l'utente nel DB tramite email
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Utente non trovato con email: " + email);
        }

        User user = userOptional.get();

        // Qui trasformiamo il nostro User (Entità) in un UserDetails (Oggetto di Spring Security)
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(), // true se attivo, false se bannato
                true, true, true, // account non scaduto, credenziali non scadute, account non bloccato
                AuthorityUtils.createAuthorityList(user.getRole()) // Ruolo: "ADMIN" o "DEFAULT"
        );
    }
}