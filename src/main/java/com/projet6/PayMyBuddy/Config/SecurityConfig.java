package com.projet6.PayMyBuddy.Config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


/**
 * Configuration de la sécurité pour l'application PayMyBuddy.
 * Cette classe configure les règles d'authentification et d'autorisation pour l'accès aux différentes
 * ressources de l'application. Elle définit les pages accessibles sans authentification, la gestion
 * du login, du logout et le chiffrement des mots de passe utilisateurs.
 */

@Configuration
@EnableMethodSecurity
@ConditionalOnWebApplication
public class SecurityConfig {

/**
 * Configure la chaîne de filtres de sécurité pour l'application.
 * - Autorise l'accès libre aux pages de connexion, d'inscription et aux ressources statiques (CSS, JS).
 * - Rend toutes les autres URLs accessibles uniquement aux utilisateurs authentifiés.
 * - Définit la page de login personnalisée et la redirection après connexion réussie.
 * - Configure la déconnexion et la redirection après déconnexion.
 */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/signup", "/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/profile", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    /**
     * Fournit un encodeur de mot de passe utilisant l'algorithme BCrypt.
     * Cet encodeur est utilisé pour hasher les mots de passe utilisateurs avant stockage
     * en base de données et pour la vérification lors de l'authentification
     * @return Un PasswordEncoder basé sur BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
