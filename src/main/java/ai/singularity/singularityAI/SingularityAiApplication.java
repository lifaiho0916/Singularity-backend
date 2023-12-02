package ai.singularity.singularityAI;

import ai.singularity.singularityAI.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

//@EnableJpaRepositories
@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class SingularityAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SingularityAiApplication.class, args);
    }

}
