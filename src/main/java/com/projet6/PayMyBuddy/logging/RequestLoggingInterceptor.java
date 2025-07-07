package com.projet6.PayMyBuddy.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.Map;

/**
 * Intercepteur Spring MVC pour le logging des requêtes HTTP entrantes.
 * Permet de journaliser la méthode HTTP, l’URL appelée, et les paramètres de requête à chaque appel reçu,
 * pour faciliter le suivi et le débogage de l’application.
 */
@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

    /**
     * Intercepte chaque requête HTTP avant qu’elle ne soit traitée par un contrôleur.
     * Journalise la date/heure, la méthode HTTP, l’URI et les paramètres de la requête.
     * @param request  La requête HTTP entrante.
     * @param response La réponse HTTP.
     * @param handler  L’objet handler ciblé par la requête.
     * @return Toujours {@code true} pour poursuivre le traitement de la requête.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String queryParams = request.getParameterMap().entrySet().stream()
                .map(e -> e.getKey() + "=" + String.join(",", e.getValue()))
                .collect(Collectors.joining("&"));

        logger.info("==> [{}] {} {} {}", LocalDateTime.now(), request.getMethod(), request.getRequestURI(), queryParams);
        return true;
    }
}
