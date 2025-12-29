package it.uniroma3.siw.siw_recipes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.siw_recipes.model.Recipe;
import it.uniroma3.siw.siw_recipes.repository.RecipeRepository;

@Service
public class RecipeService {
    
    @Autowired
    private RecipeRepository recipeRepository;

    @Transactional
    public Recipe saveRecipe(Recipe recipe){
        return recipeRepository.save(recipe);
    }

    public Recipe findById(Long id){
        return recipeRepository.findById(id).orElse(null);
    }

    public List<Recipe> findAll(){
        return (List<Recipe>) recipeRepository.findAll();
    }

}
