package com.rizq_venture.rizqconnects;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class RizqconnectsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RizqconnectsApplication.class, args);
	}

}
