package ai.singularity.singularityAI.config;

//import liquibase.integration.spring.SpringLiquibase;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@EnableConfigurationProperties(LiquibaseProperties.class)
@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	private final long MAX_AGE_SECS = 3600;

    @Value("${app.cors.allowedOrigins}")
    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
        	.addMapping("/**")
        	.allowedOrigins(allowedOrigins)
        	.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        	.allowedHeaders("*")
        	.allowCredentials(true)
        	.maxAge(MAX_AGE_SECS);
    }

//    @Bean
//    public SpringLiquibase liquibase(DataSource dataSource, LiquibaseProperties liquibaseProperties) {
//
//        SpringLiquibase liquibase = new SpringLiquibase();
//        liquibase.setChangeLog("classpath:config/liquibase/master.xml");
//        liquibase.setDataSource(dataSource);
//        liquibase.setContexts(liquibaseProperties.getContexts());
//        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
//        liquibase.setLiquibaseSchema(liquibaseProperties.getDefaultSchema());
//        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
//        liquibase.setShouldRun(liquibaseProperties.isEnabled());
//        log.debug("Configuring Liquibase");
//        return liquibase;
//    }
}
