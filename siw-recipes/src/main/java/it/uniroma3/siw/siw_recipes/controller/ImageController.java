package it.uniroma3.siw.siw_recipes.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ImageController {

    @GetMapping("/recipe-images/{id}/{filename}")
    public ResponseEntity<byte[]> getRecipeImage(@PathVariable("id") Long id, 
                                                 @PathVariable("filename") String filename) throws IOException {
        
        
        String pathRelativo = "recipe-images/" + id + "/" + filename;
        Path imagePath = Paths.get(pathRelativo);

        // --- STAMPE DI DEBUG ---
        System.out.println("------------------------------------------------");
        System.out.println("RICHIESTA ARRIVATA PER: " + filename);
        System.out.println("LA CARTELLA BASE DI JAVA (user.dir) È: " + System.getProperty("user.dir"));
        System.out.println("JAVA STA CERCANDO IL FILE QUI: " + imagePath.toAbsolutePath());
        System.out.println("IL FILE ESISTE? " + Files.exists(imagePath));
        System.out.println("------------------------------------------------");
        // ------------------------------------------------------

        if (!Files.exists(imagePath)) {
            return ResponseEntity.notFound().build();
        }

        byte[] imageBytes = Files.readAllBytes(imagePath);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);
    }
}