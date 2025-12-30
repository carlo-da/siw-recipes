package it.uniroma3.siw.siw_recipes.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.siw_recipes.model.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, Long> {
    // Per trovare ingredienti per nome
    public List<Ingredient> findByName(String name);
}
