package com.rouilleur.emcservices.swagger;

import com.rouilleur.emcservices.controller.EmcJobRestController;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by Rouilleur on 02/11/2016.
 */
@Configuration
@EnableSwagger2
@ComponentScan(basePackageClasses = {
        EmcJobRestController.class
})
public class Swagger2SpringBoot {

    @Bean
    public Docket taskrunnerApi() {
        return new Docket(
                DocumentationType.SWAGGER_2)
                    .pathMapping("/")
                ;
    }

    @Bean
    UiConfiguration uiConfig() {
        return new UiConfiguration(
                "validatorUrl",// url
                "none",       // docExpansion          => none | list
                "alpha",      // apiSorter             => alpha
                "schema",     // defaultModelRendering => schema
                UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS,
                false,        // enableJsonEditor      => true | false
                true);


    }
}


