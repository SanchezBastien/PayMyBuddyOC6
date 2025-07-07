-- Création des tables pour PayMyBuddy

CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    balance DECIMAL(10,2) NOT NULL DEFAULT 0.00
);

CREATE TABLE transaction (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    description TEXT,
    amount DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (sender_id) REFERENCES user(id),
    FOREIGN KEY (receiver_id) REFERENCES user(id)
);

CREATE TABLE connection (
    user_id INT NOT NULL,
    friend_id INT NOT NULL,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (friend_id) REFERENCES user(id)
);
