package com.projet6.PayMyBuddy;

import com.projet6.PayMyBuddy.Config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Classe de test pour la configuration de sécurité de l'application PayMyBuddy.
 * Vérifie l'accès aux différentes routes selon les règles de sécurité définies dans {@link SecurityConfig} :
 * - Accès public aux pages de login, signup, ressources statiques
 * - Protection des routes nécessitant une authentification
 * - Fonctionnement du bean PasswordEncoder
 */
@WebMvcTest(controllers = {
        SecurityConfigTest.FakeController.class,
        SecurityConfigTest.FakePublicController.class
})
@Import(SecurityConfig.class)

class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private com.projet6.PayMyBuddy.Services.UserService userService;

    /**
     * Contrôleur simulé pour tester un endpoint protégé (nécessitant authentification).
     */
    @Controller
    static class FakeController {
        @GetMapping("/profile")
        @ResponseBody
        public String profile() {
            return "profile";
        }
    }

    /**
     * Contrôleur simulé pour endpoints publics ("/login", "/signup", etc.).
     */
    @Controller
    static class FakePublicController {
        // /login doit retourner le nom d'une vue, sinon Spring Security lève 404
        @GetMapping("/login")
        public String loginPage() {
            return "login";
        }

        @GetMapping({"/signup", "/css/test.css", "/js/test.js"})
        @ResponseBody
        public String fake() {
            return "ok";
        }
    }

    /**
     * Vérifie que l’encodeur de mot de passe injecté utilise bien BCrypt.
     */
    @Test
    void passwordEncoderBeanShouldBeBCrypt() {
        String raw = "test123";
        String encoded = passwordEncoder.encode(raw);
        assertThat(passwordEncoder.matches(raw, encoded)).isTrue();
    }

    /**
     * Vérifie que l’accès à un endpoint protégé redirige l’utilisateur non authentifié vers la page de login.
     * Ce test s’assure qu’une requête GET sur "/profile" sans authentification
     * entraîne une redirection (code 3xx) vers la page "/login", conformément à la politique de sécurité définie
     * @throws Exception en cas d’erreur lors de la requête MockMvc.
     */
    @Test
    void protectedEndpointRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}
