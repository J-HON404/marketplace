package com.unicam.cs.progettoweb.marketplace.marketplace;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MarketplaceApplication {

	public static void main(String[] args) {
		// Carica il file .env
		Dotenv dotenv = Dotenv.load();
		System.setProperty("DB_URL", dotenv.get("DB_URL"));
		System.setProperty("DB_USER", dotenv.get("DB_USER"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		System.setProperty("NEO4J_URI",dotenv.get("NEO4J_URI"));
		System.setProperty("NEO4J_USERNAME",dotenv.get("NEO4J_USERNAME"));
		System.setProperty("NEO4J_PASSWORD",dotenv.get("NEO4J_PASSWORD"));
		SpringApplication.run(MarketplaceApplication.class, args);
	}

}

