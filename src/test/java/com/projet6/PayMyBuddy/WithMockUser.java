package com.projet6.PayMyBuddy;

/**
 * Annotation personnalisée permettant de simuler un utilisateur authentifié dans les tests.
 * Utilisée pour exécuter des tests avec un contexte de sécurité simulant un utilisateur connecté,
 * afin de tester les contrôleurs sécurisés ou toute logique dépendant de l’authentification.
 */
public @interface WithMockUser {
}
