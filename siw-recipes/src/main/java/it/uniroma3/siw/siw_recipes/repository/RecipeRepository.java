package it.uniroma3.siw.siw_recipes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.siw_recipes.model.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, Long>{
    

    public Optional<Recipe> findById(Long id);// Cerca una ricetta tramite l'id

    public List<Recipe> findAll();// Restituisce tutte le ricette
}
