package com.rouilleur;

import com.rouilleur.emcservices.config.TaskrunnerConfig;
import com.rouilleur.emcservices.jobs.repository.EmcJobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;



@SpringBootApplication
public class TaskrunnerApplication {
    private final static Logger logger = LoggerFactory.getLogger(TaskrunnerApplication.class);

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
