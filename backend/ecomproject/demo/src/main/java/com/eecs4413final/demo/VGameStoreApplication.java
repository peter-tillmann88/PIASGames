package com.eecs4413final.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SpringBootApplication
public class VGameStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(VGameStoreApplication.class, args);
	}

}