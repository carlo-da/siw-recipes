package it.uniroma3.siw.siw_recipes.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users") 
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    @Email
    @Column(unique = true) // L'email deve essere univoca
    private String email;

    @NotBlank
    private String password;

    // Ruolo: "ADMIN" o "DEFAULT"
    // I requisiti distinguono tra Utenti Registrati e Amministratori
    private String role; 

    // Stato: true = attivo, false = bannato
    // Soddisfa il requisito: "bannare utenti registrati"
    private boolean enabled;

    /* RELAZIONI */
    // Un utente può scrivere molte ricette
    // Useremo 'author' nella classe Recipe
    // @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    // private List<Recipe> recipes;
    
    // Un utente può scrivere molte recensioni
    // @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    // private List<Review> reviews;

    // COSTRUTTORI
    public User() {
        this.enabled = true; //L'utente è attivo per default
        this.role = "DEFAULT";
    }

    // ------GETTERS & SETTERStandard-------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    // -----HASHCODE & EQUALS------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email) && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}