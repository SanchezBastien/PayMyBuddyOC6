package com.projet6.PayMyBuddy.Services;

import com.projet6.PayMyBuddy.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Service de gestion des détails utilisateurs pour l'authentification Spring Security.
 * Cette classe permet à Spring Security de charger les informations d'un utilisateur
 * (email, mot de passe, rôles) à partir de la base de données via le UserRepository.
 * Elle est utilisée lors du processus de connexion pour authentifier les utilisateurs.
 */

@Service
public class MyUserDetailsService implements UserDetailsService {

    /**
     * Repository pour accéder aux utilisateurs stockés en base de données.
     */
    @Autowired
    private UserRepository userRepository;

/**
 * Charge les détails d'un utilisateur à partir de son adresse email.
 * Cette méthode est appelée automatiquement par Spring Security lors de la tentative
 * de connexion d'un utilisateur. Elle recherche l'utilisateur dans la base de données,
 * construit un objet UserDetails si l'utilisateur est trouvé, ou lance une exception sinon.
 */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),  // doit être encodé
                        Collections.emptyList()
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + email));
    }
}
