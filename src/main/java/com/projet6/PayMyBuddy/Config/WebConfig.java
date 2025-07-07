package com.projet6.PayMyBuddy.Config;

import com.projet6.PayMyBuddy.logging.RequestLoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration Spring MVC de l’application PayMyBuddy.
 * Permet de personnaliser le comportement de Spring MVC, notamment en ajoutant des intercepteurs
 * pour le logging des requêtes HTTP.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private RequestLoggingInterceptor requestLoggingInterceptor;

    /**
     * Ajoute les intercepteurs personnalisés à la chaîne de traitement des requêtes Spring MVC.
     * Ici, l’intercepteur {@link RequestLoggingInterceptor} permet de journaliser chaque requête HTTP entrante
     * @param registry Le registre des intercepteurs à configurer.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLoggingInterceptor);
    }
}
