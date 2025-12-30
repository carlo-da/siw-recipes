package it.uniroma3.siw.siw_recipes.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.siw_recipes.model.Review;


public interface ReviewRepository extends CrudRepository<Review, Long> {
    // Per ora ci bastano i metodi base 
    public List<Review> findAll();
}
