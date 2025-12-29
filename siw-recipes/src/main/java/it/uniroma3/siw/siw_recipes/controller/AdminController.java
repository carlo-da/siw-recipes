package it.uniroma3.siw.siw_recipes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.uniroma3.siw.siw_recipes.model.User;
import it.uniroma3.siw.siw_recipes.repository.RecipeRepository;
import it.uniroma3.siw.siw_recipes.repository.UserRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private UserRepository userRepository;
    @Autowired private RecipeRepository recipeRepository;

    // -----VISUALIZZAZIONE UTENTI------
    @GetMapping("/users")
    public String adminUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users"; // Creeremo questa vista dentro una sottocartella
    }

    // ------BAN UTENTE-----
    @PostMapping("/users/ban/{id}")
    public String banUser(@PathVariable("id") Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null && !user.getRole().equals("ADMIN")) { // Non banniamo gli admin!
            user.setEnabled(false); // Disabilita l'utente
            userRepository.save(user);
        }
        return "redirect:/admin/users";
    }

    // ------RIATTIVARE UTENTE-------
    @PostMapping("/users/enable/{id}")
    public String enableUser(@PathVariable("id") Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setEnabled(true);
            userRepository.save(user);
        }
        return "redirect:/admin/users";
    }
    
    // ------CANCELLAZIONE RICETTA------
    @PostMapping("/recipe/delete/{id}")
    public String deleteRecipe(@PathVariable("id") Long id) {
        recipeRepository.deleteById(id);
        return "redirect:/recipe/all";
    }
}
