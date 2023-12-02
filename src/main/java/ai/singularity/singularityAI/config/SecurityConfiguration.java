package ai.singularity.singularityAI.config;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsUtils;

import ai.singularity.singularityAI.security.oauth2.CustomOAuth2UserService;
import ai.singularity.singularityAI.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import ai.singularity.singularityAI.security.oauth2.OAuth2AuthenticationFailureHandler;
import ai.singularity.singularityAI.security.oauth2.OAuth2AuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfiguration {

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        // @formatter:off
//        http
//                .cors(AbstractHttpConfigurer::disable)
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
//                        .requestMatchers(CorsUtils::isCorsRequest).permitAll()
//                        .requestMatchers("/swagger-ui/**").authenticated()
//                        .requestMatchers("/v3/api-docs/**").authenticated()
//                        .requestMatchers("/v1/**").authenticated()
//                        .requestMatchers("/actuator/health").permitAll()
//                        .requestMatchers("/actuator/health/**").permitAll()
//                        .requestMatchers("/actuator/info").permitAll()
//                        .requestMatchers("/actuator/prometheus").permitAll()
//                        .requestMatchers("/actuator/**").permitAll())
//                .oauth2Login();
//
//        return http.build();
//        // @formatter:on
//    }
//

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Autowired
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    
    @Bean
    ModelMapper getModelMapper() {
        return new ModelMapper();
    }

    /*
      By default, Spring OAuth2 uses HttpSessionOAuth2AuthorizationRequestRepository to save
      the authorization request. But, since our service is stateless, we can't save it in
      the session. We'll save the request in a Base64 encoded cookie instead.
    */
    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        		.cors(AbstractHttpConfigurer::disable)
        		.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .authorizeHttpRequests(authorize -> authorize
                	.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                	.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                	.requestMatchers(CorsUtils::isCorsRequest).permitAll()
                	.requestMatchers("/swagger-ui/**").authenticated()
                	.requestMatchers("/v3/api-docs/**").authenticated()
                	.requestMatchers("/v1/**").authenticated()
                	.requestMatchers("/actuator/health").permitAll()
                	.requestMatchers("/actuator/health/**").permitAll()
                	.requestMatchers("/actuator/info").permitAll()
                	.requestMatchers("/actuator/prometheus").permitAll()
                	.requestMatchers("/actuator/**").permitAll()
                	.requestMatchers("/auth/**", "/oauth2/**").permitAll())
                .oauth2Login()
                    .authorizationEndpoint()
                        .baseUri("/oauth2/authorize")
                        .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                        .and()
                    .redirectionEndpoint()
                        .baseUri("/oauth2/callback/*")
                        .and()
                    .userInfoEndpoint()
                        .userService(customOAuth2UserService)
                        .and()
                    .successHandler(oAuth2AuthenticationSuccessHandler)
                    .failureHandler(oAuth2AuthenticationFailureHandler);

        // Add our custom Token based authentication filter
//        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
