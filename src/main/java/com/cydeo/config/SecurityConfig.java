package com.cydeo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
//
//        List<UserDetails> userList = new ArrayList<>();
//
//        userList.add(
//                new User("mike", encoder.encode("password"),
//                        Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")))
//        );
//        userList.add(
//                new User("ozzy", encoder.encode("password"),
//                        Arrays.asList(new SimpleGrantedAuthority("ROLE_MANAGER")))
//        );                                         // NEED _ (underscore) ^^
//
//        return new InMemoryUserDetailsManager(userList);
//    }

    @Bean       // used to allow certain views without Security requirement
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeRequests()
//                .antMatchers("/user/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasAuthority("Admin")
//                .antMatchers("/project/**").hasRole("MANAGER")
//                .antMatchers("/task/employee/**").hasRole("EMPLOYEE")
//                .antMatchers("/task/**").hasRole("MANAGER")
                // ^^ allowing certain Roles to view certain pages
//                .antMatchers("/task/**").hasAnyRole("EMPLOYEE", "ADMIN")
//                .antMatchers("/task/**").hasAuthority("ROLE_EMPLOYEE")
//                                               // NEED _ (underscore) ^^
                .antMatchers(
                        "/",
                        "/login",
                        "/fragments/**",
                        "/assets/**",
                        "/images/**"
                ).permitAll()   // ^^ allows all users to see the above views
                .anyRequest().authenticated()
        // any views INSIDE the above need to be authenticated prior to access
            // ex. Employee cannot access Manager tab
                .and()
//                .httpBasic()    // login pop up box
                .formLogin()                // using our own login form
                    .loginPage("/login")
                    .defaultSuccessUrl("/welcome")
                    .failureUrl("/login?error=true")
                    .permitAll()
                .and().build();
    }

}
