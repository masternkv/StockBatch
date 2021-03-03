package com.stock.batch;

import java.util.Arrays;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@SpringBootApplication
public class SpringBootBatchProjectApplication  implements ApplicationContextAware{

	
	private static ApplicationContext context;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootBatchProjectApplication.class, args);
	}


	 public static ApplicationContext getApplicationContext() {
	        return context;
	    }

	@Override
	public void setApplicationContext(ApplicationContext ac) throws BeansException {
		
		context = ac;
	}
}
