package it.uniroma3.siw.siw_recipes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.uniroma3.siw.siw_recipes.model.Category;
import it.uniroma3.siw.siw_recipes.model.User;
import it.uniroma3.siw.siw_recipes.repository.CategoryRepository;
import it.uniroma3.siw.siw_recipes.repository.RecipeRepository;
import it.uniroma3.siw.siw_recipes.repository.UserRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private UserRepository userRepository;
    @Autowired private RecipeRepository recipeRepository;
    @Autowired private CategoryRepository categoryRepository;

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

    // ---------CANCELLAZIONE UTENET---------
    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null && !user.getRole().equals("ADMIN")) {
            userRepository.delete(user);
        }
        return "redirect:/admin/users";
    }
    
    // ------CANCELLAZIONE RICETTA------
    @PostMapping("/recipe/delete/{id}")
    public String deleteRecipe(@PathVariable("id") Long id) {
        recipeRepository.deleteById(id);
        return "redirect:/recipe/all";
    }

    // -------GESTIONE CATEGORIE-------
    @GetMapping("/categories")
    public String adminCategories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("category", new Category()); // Per il form di inserimento
        return "admin/categories";
    }

    @PostMapping("/categories")
    public String addCategory(@ModelAttribute("category") Category category, Model model) {
        if (!categoryRepository.existsByName(category.getName())) {
            categoryRepository.save(category);
        }
        return "redirect:/admin/categories";
    }
    
    @PostMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        // Per ora assumiamo di cancellare solo categorie vuote
        categoryRepository.deleteById(id);
        return "redirect:/admin/categories";
    }
}
