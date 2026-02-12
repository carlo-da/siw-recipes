package it.uniroma3.siw.siw_recipes.authentication; 

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import it.uniroma3.siw.siw_recipes.model.User;
import it.uniroma3.siw.siw_recipes.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        
        // Recuperiamo i dati da Google
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("given_name");
        String surname = oAuth2User.getAttribute("family_name"); // Potrebbe essere null

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            // REGISTRAZIONE AUTOMATICA (JIT Provisioning)
            user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setSurname(surname != null ? surname : "");
            user.setRole("DEFAULT");
            user.setEnabled(true);
            
            // Password fittizia
            user.setPassword(passwordEncoder.encode("OAUTH_GOOGLE_USER")); 
            
            userRepository.save(user);
        }

        // Reindirizza alla Home Page dopo il login
        super.setDefaultTargetUrl("/");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}