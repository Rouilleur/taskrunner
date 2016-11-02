package com.rouilleur;

import com.rouilleur.emcservices.jobs.EmcJob;
import com.rouilleur.emcservices.jobs.EmcJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;



@SpringBootApplication
public class TaskrunnerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskrunnerApplication.class, args);
	}


    @Bean
    public CommandLineRunner demo(EmcJobRepository repository) {
        return (args) -> {
            System.out.println("Just another CMLRunner");
            repository.save(new EmcJob("Another submiter"));
        };
    }

}



@Component
class  bootstrappDB implements CommandLineRunner{

    @Autowired
    EmcJobRepository repository;

    @Override
    public void run(String... args) throws Exception{
        System.out.println("I'm here");
        this.repository.save(new EmcJob("Submiter 1"));

        return;
    }
}
