package it.uniroma3.siw.siw_recipes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.siw_recipes.model.Recipe;
import it.uniroma3.siw.siw_recipes.model.Review;
import it.uniroma3.siw.siw_recipes.model.User;
import it.uniroma3.siw.siw_recipes.repository.RecipeRepository;
import it.uniroma3.siw.siw_recipes.repository.ReviewRepository;
import it.uniroma3.siw.siw_recipes.repository.UserRepository;
import jakarta.validation.Valid;

@Controller
public class ReviewController {

    @Autowired private ReviewRepository reviewRepository;
    @Autowired private RecipeRepository recipeRepository;
    @Autowired private UserRepository userRepository;

    @PostMapping("/recipe/{recipeId}/review")
    public String newReview(@Valid @ModelAttribute("review") Review review,
                            BindingResult bindingResult,
                            @PathVariable("recipeId") Long recipeId,
                            Model model) {
        
        Recipe recipe = recipeRepository.findById(recipeId).get();
        
        // Se ci sono errori nel form (es. titolo vuoto), ricarichiamo la pagina
        if (bindingResult.hasErrors()) {
            model.addAttribute("recipe", recipe);
            return "recipeDetail";
        }

        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findByEmail(userDetails.getUsername()).get();// Chi è l'autore della recensione?      
        review.setRecipe(recipe);
        review.setAuthor(currentUser);        
        recipe.getReviews().add(review); // Aggiungiamo alla lista della ricetta

        reviewRepository.save(review); // Salva
        
        return "redirect:/recipe/" + recipeId;
    }
    
}
