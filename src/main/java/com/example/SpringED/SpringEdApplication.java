package com.example.SpringED;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SpringEdApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringEdApplication.class, args);
	}

}
//brew services start mongodb-community@8.0
//brew services stop mongodb-community@8.0