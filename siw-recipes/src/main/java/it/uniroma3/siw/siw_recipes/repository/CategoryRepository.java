package it.uniroma3.siw.siw_recipes.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.siw_recipes.model.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    public List<Category> findAll();
    public boolean existsByName(String name);

}
