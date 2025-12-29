package it.uniroma3.siw.siw_recipes.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String title;

    @Column(nullable = true, length = 64)
    private String imageFileName; // Immagine 

    @Column(length = 2000)
    private String description; // Descrizione breve

    @Column(length = 5000)
    private String procedureText; // Procedimento completo

    @Min(1)
    private int cookTime; // Tempo di preparazione in minuti

    // Difficoltà da 1 a 5 
    @Min(1) @Max(5)
    private int difficulty;

    // Relazione N-1: Molte ricette hanno un solo autore
    @ManyToOne
    private User author;
    
    // Relazione N-1: Ogni ricetta appartiene a una categoria
    @ManyToOne
    private Category category;

    // Relazione 1-N: Una ricetta ha molti ingredienti
    // @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    // private List<Ingredient> ingredients;

    public Recipe() {
    }

    // -----GETTERS & SETTERS-----
    public Long getId() { 
        return id; 
    }
    public void setId(Long id) {
         this.id = id; 
        }

    public String getTitle() { 
        return title; 
    }
    public void setTitle(String title) { 
        this.title = title; 
    }

    public String getDescription() { 
        return description; 
    }
    public void setDescription(String description) { 
        this.description = description; 
    }
    
    public String getProcedureText() { 
        return procedureText; 
    }
    public void setProcedureText(String procedureText) { 
        this.procedureText = procedureText; 
    }

    public int getCookTime() { return cookTime; }
    public void setCookTime(int cookTime) { 
        this.cookTime = 
        cookTime; }

    public int getDifficulty() { 
        return difficulty; 
    }
    public void setDifficulty(int difficulty) { 
        this.difficulty = difficulty; 
    }

    public User getAuthor() { 
        return author; 
    }
    public void setAuthor(User author) { 
        this.author = author; 
    }
    
    public String getImagePath() {
        if (imageFileName == null || id == null) return null;
        return "/recipe-images/" + id + "/" + imageFileName;
    }

    public String getImageFileName() {
        return imageFileName;
    }
    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Recipe other = (Recipe) obj;
        return Objects.equals(id, other.id) && Objects.equals(title, other.title);
    }
}
