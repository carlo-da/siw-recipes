package it.uniroma3.siw.siw_recipes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.siw_recipes.model.User;
import it.uniroma3.siw.siw_recipes.repository.UserRepository;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // -----LOGIN-----
    @GetMapping("/login")
    public String showLoginForm() {
        return "formLogin"; // Ritorna formLogin.html
    }

    // -----HOME PAGE-----
    @GetMapping(value = {"/", "/index"})
    public String index(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();//Se l'utente è loggato, possiamo passare il suo nome alla vista
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            model.addAttribute("username", userDetails.getUsername());
        }
        return "index"; // Ritorna index.html
    }

    // -----REGISTRAZIONE (GET: Mostra il form)-----
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User()); // Passiamo un oggetto vuoto al form
        return "formRegister";
    }

    // -----REGISTRAZIONE (POST: Salva i dati)-----
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, 
                               BindingResult bindingResult, 
                               Model model) {
        
        if (userRepository.existsByEmail(user.getEmail())) {//Controllo se l'email esiste già
            bindingResult.rejectValue("email", "error.user", "Questa email è già registrata");
        }
        if (bindingResult.hasErrors()) {
            return "formRegister";//Se ci sono errori di validazione (es. campi vuoti o email duplicata), ricarica la pagina
        }
 
        user.setPassword(passwordEncoder.encode(user.getPassword()));//Salvataggio - Cifriamo la password prima di salvare
        user.setRole("DEFAULT"); // Assegniamo il ruolo base
        user.setEnabled(true);   // Attiviamo l'utente
        userRepository.save(user);

        return "redirect:/login?success";//Redirect al login
    }
}
