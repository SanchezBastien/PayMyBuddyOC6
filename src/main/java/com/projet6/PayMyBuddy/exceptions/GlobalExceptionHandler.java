package com.projet6.PayMyBuddy.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Gestionnaire global des exceptions pour l’application PayMyBuddy.
 * Intercepte les exceptions non gérées lancées par les contrôleurs REST
 * et fournit des réponses HTTP appropriées, avec journalisation des erreurs.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Gère les erreurs liées à la non-disponibilité d’une ressource statique (404).
     * @param ex L’exception {@link NoResourceFoundException} capturée.
     * @return Une réponse HTTP 404 Not Found.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> handleNoResourceFound(NoResourceFoundException ex) {
        // Log en mode DEBUG ou ne log rien du tout
        logger.debug("Requête ressource statique non trouvée : " + ex.getResourcePath());
        return ResponseEntity.notFound().build();
    }

    /**
     * Gère toutes les exceptions non spécifiques du système.
     * Journalise l’erreur et retourne un message d’erreur générique avec le code HTTP 500.
     * @param ex L’exception inattendue.
     * @return Une réponse HTTP 500 Internal Server Error avec un message générique.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        logger.error("Erreur globale attrapée :", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Une erreur interne est survenue.");
    }
}