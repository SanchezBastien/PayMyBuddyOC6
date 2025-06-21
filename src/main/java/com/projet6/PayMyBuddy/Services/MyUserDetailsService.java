package com.projet6.PayMyBuddy.Services;

import com.projet6.PayMyBuddy.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

//Spring Security appelle cette méthode lorsqu’un utilisateur essaie de se connecter.
//Elle est responsable de retrouver l’utilisateur depuis la base de données, de construire un objet UserDetails,
//et de le retourner à Spring Security pour effectuer l’authentification.
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

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
