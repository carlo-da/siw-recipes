package it.uniroma3.siw.siw_recipes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Ingredient {

    public Ingredient(){}

    public Ingredient(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;     
    private String quantity; 

    // Molti ingredienti appartengono a Una ricetta
    @ManyToOne
    private Recipe recipe;

    // ------GETTERS E SETTERS------
    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public String getName() { 
        return name; 
    }

    public void setName(String name) { 
        this.name = name; 
    }

    public String getQuantity() { 
        return quantity; 
    }

    public void setQuantity(String quantity) { 
        this.quantity = quantity; 
    }

    public Recipe getRecipe() { 
        return recipe; 
    }

    public void setRecipe(Recipe recipe) { 
        this.recipe = recipe; 
    }
}