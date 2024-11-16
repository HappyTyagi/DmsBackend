package com.dmsBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan
public class DmsApplication {

	public static void main(String[] args) {

		SpringApplication.run(DmsApplication.class, args);
	}

}
