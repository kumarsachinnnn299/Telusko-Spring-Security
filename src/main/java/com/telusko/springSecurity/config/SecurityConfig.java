package com.telusko.springSecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity//this will prevent default flow of spring security  filter chains and will make this as our default flow
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {



        http.csrf(customizer-> customizer.disable());//we are disabling the csrf
        http.authorizeHttpRequests(request->request.anyRequest().authenticated());//All request should be authenticated
//        http.formLogin(Customizer.withDefaults());//it will enable the login form that we see by default with spring security
        http.httpBasic(Customizer.withDefaults());//this will allow basic authentication through Postman

        //To handle csrf, one way is to make the application stateless
        http.sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));//while using this disable http.formlogin
        //otherwise it will keep asking for username and password again and again as each resource is a new resource and it will act as a new session everytime
        return http.build();


        /*
        * Another way of writing upper code: Buildup pattern: one object is passed to multiple fns one by one
        *
        * return http
        *           .csrf(Customizer-> Customizer.disable())
        *           .authorizeHttpRequests(request->request.anyRequest().authenticated())
        *           .httpBasic(Customizer.withDefaults())
        *           .sessionManagement(session->
        *                       session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        *           .build();
        * */
    }

    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails user1=User
                .withDefaultPasswordEncoder()
                .username("sachin")
                .password("s@123")
                .roles("USER")
                .build();
        UserDetails user2=User
                .withDefaultPasswordEncoder()
                .username("harsh")
                .password("h@123")
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user1,user2);
    }


}
