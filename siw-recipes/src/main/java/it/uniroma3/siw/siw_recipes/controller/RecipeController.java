package it.uniroma3.siw.siw_recipes.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.siw_recipes.model.Recipe;
import it.uniroma3.siw.siw_recipes.model.Review;
import it.uniroma3.siw.siw_recipes.model.User;
import it.uniroma3.siw.siw_recipes.repository.CategoryRepository;
import it.uniroma3.siw.siw_recipes.repository.RecipeRepository;
import it.uniroma3.siw.siw_recipes.repository.UserRepository;
import it.uniroma3.siw.siw_recipes.service.RecipeService;
import it.uniroma3.siw.siw_recipes.util.FileUploadUtil;
import jakarta.validation.Valid;

@Controller
public class RecipeController {

    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RecipeRepository recipeRepository;

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
        model.addAttribute("review", new Review());
        return "recipeDetail";
    }

    // ------GESTIONE OPERAZIONE DI INSERIMENTO (Solo Utenti Registrati)------
    @GetMapping("/recipe/form")
    public String getRecipeForm(Model model) {
        model.addAttribute("recipe", new Recipe());
        model.addAttribute("categories", categoryRepository.findAll());
        return "recipeForm";
    }

    @PostMapping("/recipe")
    public String newRecipe(@Valid @ModelAttribute("recipe") Recipe recipe,
            BindingResult bindingResult, @RequestParam("image") MultipartFile multipartFile,
            Model model) throws IOException {

        if (bindingResult.hasErrors()) {
            return "recipeForm";
        }
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        recipe.setImageFileName(fileName); // Gestione immagine
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findByEmail(userDetails.getUsername()).get();
        recipe.setAuthor(currentUser); // Gestione autore
        Recipe savedRecipe = recipeService.saveRecipe(recipe);
        String uploadDir = "recipe-images/" + savedRecipe.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        return "redirect:/recipe/" + savedRecipe.getId() + "/ingredients";
    }

    //---------VISIONE RICETTE PROPRIE-------
    @GetMapping("/myRecipes")
    public String myRecipes(Model model) {
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findByEmail(userDetails.getUsername()).get();// Recupera le ricette dell'utente loggato
        List<Recipe> myRecipes = recipeRepository.findByAuthor(currentUser);
        model.addAttribute("recipes", myRecipes);
        
        return "myRecipes";
    }

    //--------CANCELLAZIONE RICETTA---------
    @GetMapping("/recipe/delete/{id}")
    public String deleteRecipe(@PathVariable("id") Long id) {
        Recipe recipe = recipeRepository.findById(id).get();
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findByEmail(userDetails.getUsername()).get();
        if (recipe.getAuthor().getId().equals(currentUser.getId())) { // Cancella SOLO se sono l'autore OPPURE se sono Admin
            
            // Cancella ricetta (e a cascata ingredienti, recensioni, etc)
            recipeRepository.delete(recipe);
        }
        return "redirect:/myRecipes";
    }

    //-------MODIFICA RICETTA-----------
    @GetMapping("/recipe/edit/{id}")
    public String editRecipeForm(@PathVariable("id") Long id, Model model) {
        Recipe recipe = recipeRepository.findById(id).get();
        
        // Controllo sicurezza (come per delete)
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findByEmail(userDetails.getUsername()).get();
        
        if (!recipe.getAuthor().getId().equals(currentUser.getId())) {
            return "redirect:/recipe/" + id;
        }

        model.addAttribute("recipe", recipe);
        model.addAttribute("categories", categoryRepository.findAll());
        return "recipeEditForm";
    }

    // Salva le modifiche fatte
    @PostMapping("/recipe/update/{id}")
    public String updateRecipe(@PathVariable("id") Long id, 
                               @ModelAttribute("recipe") Recipe formData,
                               @RequestParam("image") MultipartFile multipartFile) throws IOException {
        
        Recipe recipe = recipeRepository.findById(id).get();
        recipe.setTitle(formData.getTitle());
        recipe.setDescription(formData.getDescription());
        recipe.setCookTime(formData.getCookTime());
        recipe.setDifficulty(formData.getDifficulty());
        recipe.setProcedureText(formData.getProcedureText());
        recipe.setCategory(formData.getCategory());

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            recipe.setImageFileName(fileName);
            // Salvataggio 
            String uploadDir = "recipe-images/" + recipe.getId();
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }

        recipeRepository.save(recipe);
        return "redirect:/recipe/" + id;
    }

    //-------CERCA RICETTA------
    @PostMapping("/search")
    public String searchRecipes(Model model, @RequestParam String keyword) {
        List<Recipe> recipes = recipeRepository.findByTitleContainingIgnoreCase(keyword);
        model.addAttribute("recipes", recipes);
        return "recipeList";
    }

}
