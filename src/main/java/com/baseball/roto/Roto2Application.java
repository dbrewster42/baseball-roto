package com.baseball.roto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Roto2Application {

	public static void main(String[] args) {
		System.out.println(System.getProperty("java.class.path"));
		SpringApplication.run(Roto2Application.class, args);
	}

}
