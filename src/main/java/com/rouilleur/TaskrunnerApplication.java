package com.rouilleur;

import com.rouilleur.emcservices.jobs.EmcJobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;



@SpringBootApplication
public class TaskrunnerApplication {
    final static Logger logger = LoggerFactory.getLogger(TaskrunnerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TaskrunnerApplication.class, args);
	}


    @Bean
    public CommandLineRunner init(EmcJobRepository repository) {
        return (args) -> {
            logger.info("Initializing application");
            repository.init();
        };
    }

}
