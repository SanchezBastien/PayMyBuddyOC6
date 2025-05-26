package com.projet6.PayMyBuddy;

import com.projet6.PayMyBuddy.Model.Connection;
import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Model.Transaction;
import com.projet6.PayMyBuddy.Services.ConnectionService;
import com.projet6.PayMyBuddy.Services.TransactionService;
import com.projet6.PayMyBuddy.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PayMyBuddyApplication implements CommandLineRunner {

	@Autowired
	private ConnectionService connectionService;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(PayMyBuddyApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		//READ USERS CONNECTION TRANSACTIONS
		// USERS
		Iterable<User> users = userService.getUser();
		users.forEach((User product) ->
				System.out.println(product.getUsername()));

		// CONNECTIONS
		Iterable<Connection> connections = connectionService.getConnection();
		connections.forEach((Connection product) ->
				System.out.println(product.getUser().getUsername() + " -> " + product.getFriend().getUsername()));

		// TRANSACTIONS
		Iterable<Transaction> transactions = transactionService.getTransaction();
		transactions.forEach((Transaction product) ->
				System.out.println(
						product.getSender().getUsername() + " -> " +
								product.getReceiver().getUsername() + " : " +
								product.getAmount() + " (" + product.getDescription() + ")"));
	}
}
