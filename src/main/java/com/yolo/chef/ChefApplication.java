package com.yolo.chef;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChefApplication {
	public static void main(String[] args) {


		try {
			Dotenv dotenv = Dotenv.load();
			System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
			System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
			System.setProperty("DB_URL", dotenv.get("DB_URL"));
			System.setProperty("DB_SCHEMA_NAME", dotenv.get("DB_SCHEMA_NAME"));
			SpringApplication.run(ChefApplication.class, args);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
