# Modèle Physique de Données (MPD)

## 'user'
| Colonne   | Type          | Contraintes         |
|-----------|---------------|---------------------|
| id        | INT           | PK, Auto-incrément  |
| username  | VARCHAR(255)  | NOT NULL            |
| email     | VARCHAR(255)  | NOT NULL, UNIQUE    |
| password  | VARCHAR(255)  | NOT NULL            |
| balance   | DECIMAL(10,2) | NOT NULL, DEFAULT 0 |

## 'transaction'
| Colonne     | Type          | Contraintes                |
|-------------|---------------|----------------------------|
| id          | INT           | PK, Auto-incrément         |
| sender_id   | INT           | FK → user(id), NOT NULL    |
| receiver_id | INT           | FK → user(id), NOT NULL    |
| description | TEXT          |                            |
| amount      | DECIMAL(10,2) | NOT NULL                   |

## 'connection'
| Colonne   | Type | Contraintes                     |
|-----------|------|---------------------------------|
| user_id   | INT  | PK, FK → user(id), NOT NULL     |
| friend_id | INT  | PK, FK → user(id), NOT NULL     |

**Clé primaire composée : ('user_id', 'friend_id')
