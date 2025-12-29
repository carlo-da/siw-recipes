package it.uniroma3.siw.siw_recipes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.siw_recipes.model.Recipe;
import it.uniroma3.siw.siw_recipes.model.User;
import it.uniroma3.siw.siw_recipes.repository.UserRepository;
import it.uniroma3.siw.siw_recipes.service.RecipeService;
import jakarta.validation.Valid;

@Controller
public class RecipeController {

    @Autowired private RecipeService recipeService;
    @Autowired private UserRepository userRepository;

    // -----VISUALIZZAZIONE PUBBLICA-----

    @GetMapping("/recipe/all")
    public String getRecipes(Model model) {
        model.addAttribute("recipes", recipeService.findAll());
        return "recipeList";
    }

    @GetMapping("/recipe/{id}")
    public String getRecipe(@PathVariable("id") Long id, Model model) {
        Recipe recipe = recipeService.findById(id);
        model.addAttribute("recipe", recipe);
        return "recipeDetail";
    }

    // ------GESTIONE OPERAZIONE DI INSERIMENTO (Solo Utenti Registrati)------

    @GetMapping("/recipe/form")
    public String getRecipeForm(Model model) {
        model.addAttribute("recipe", new Recipe());
        return "recipeForm";
    }

    @PostMapping("/recipe")
    public String newRecipe(@Valid @ModelAttribute("recipe") Recipe recipe, 
                            BindingResult bindingResult, Model model) {
        
        if (bindingResult.hasErrors()) {
            return "recipeForm";
        }
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();// Recuperiamo l'utente corrente dalla sessione di sicurezza
        User currentUser = userRepository.findByEmail(userDetails.getUsername()).get();
        recipe.setAuthor(currentUser); //Impostiamo l'autore della ricetta
        recipeService.saveRecipe(recipe);
        return "redirect:/recipe/" + recipe.getId();// Redirect alla pagina della nuova ricetta
    }
}