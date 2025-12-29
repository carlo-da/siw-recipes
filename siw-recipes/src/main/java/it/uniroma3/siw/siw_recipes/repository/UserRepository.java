package it.uniroma3.siw.siw_recipes.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.siw_recipes.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    
    // Metodo necessario per l'autenticazione
    public Optional<User> findByEmail(String email);// Cerca un utente tramite la mail

    public boolean existsByEmail(String email);// Verifica se esiste già una mail
}
