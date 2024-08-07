package com.tpd.twitch;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.sql.DataSource;

/*the AppConfig class is responsible for configuring Spring Security settings for authentication and authorization,
including rules for permitting or restricting access to various paths and resources. It also customizes the behavior
of the authentication and authorization process, as well as the handling of login, logout, and error situations.*/

@Configuration
public class AppConfig {

    //@Bean means I have this object, and I want to provide it to you. The reason why other methods don't have @Bean
    // is that if you directly write @Service on the class, you don't need to write @Bean. If you don't write @Bean
    // here, Spring won't find this API and won't be able to execute it.
    @Bean
    UserDetailsManager users(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }
    // Spring stores passwords as hashed, not the original passwords set by the user.
    // Why provide an additional option instead of relying on Spring Security's default?
    // Because users may want extra security measures.
    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    /* If this file doesn't exist, running the program won't execute, and it will prompt you to log in. We added this file to bypass login.

    Security Filter Chain, we need to configure the security of different paths and also the future frontend assets.
    By default, everything is protected, we dont want that. In AppCOnfig, add the following customization
    for SecurityFilterChain */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        LogoutConfigurer<HttpSecurity> httpSecurityLogoutConfigurer = http
                .csrf().disable()
                .authorizeHttpRequests(auth ->
                        auth    //The official authorization registry template provides us with a format to fill in the required information one by one.
                                // The backend allows basic information to be downloaded to the browser at any time. Even without logging in, some basic information can be accessed.
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                .requestMatchers(HttpMethod.GET, "/", "/index.html", "/*.json", "/*.png", "/static/**").permitAll()
                                // Open the following information. Unregistered users can access the following HTTP endpoints:
                                .requestMatchers("/hello/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/login", "/register", "/logout").permitAll()  // No registration is required, and users don't need to log in to access the information.
                                .requestMatchers(HttpMethod.GET, "/recommendation", "/game", "/search").permitAll()
                                // Other information requires authentication to access.
                                // .requestMatchers(HttpMethod.GET, "/favorite").permitAll()
                                .anyRequest().authenticated()
                )
                // The following are Fluent APIs. They specify what will happen if authorization and authentication fail. If not specified, Spring Boot will redirect to the login page again.
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))    // The frontend will receive a 401 UNAUTHORIZED error.
                .and()
                // form based or session based authentication.
                .formLogin()  // session based authentication
                .successHandler((req, res, auth) -> res.setStatus(HttpStatus.NO_CONTENT.value()))
                .failureHandler(new SimpleUrlAuthenticationFailureHandler())   // It will inform you that an error has occurred.
                .and()
                .logout()
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT));
        return http.build();
    }
}
