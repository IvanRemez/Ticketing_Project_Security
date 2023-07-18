package com.cydeo.config;

import com.cydeo.service.SecurityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    private final SecurityService securityService;
    private final AuthSuccessHandler authSuccessHandler;

    public SecurityConfig(SecurityService securityService, AuthSuccessHandler authSuccessHandler) {
        this.securityService = securityService;
        this.authSuccessHandler = authSuccessHandler;
    }

    // MANUAL USER CREATION:

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
                .antMatchers("/project/**").hasAuthority("Manager")
                .antMatchers("/task/employee/**").hasAuthority("Employee")
                .antMatchers("/task/**").hasAuthority("Manager")
                // ^^ allowing certain Roles to view certain pages
//                .antMatchers("/task/**").hasAnyRole("EMPLOYEE", "ADMIN")
//                .antMatchers("/task/**").hasAuthority("ROLE_EMPLOYEE")
//                                                 // NEED _ ^^ (underscore)
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
//                    .defaultSuccessUrl("/welcome")
                .successHandler(authSuccessHandler)
                    .failureUrl("/login?error=true")
                    .permitAll()
                .and()
                .logout()                       // logout function
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                // ^^ specifying to Spring Security which UI element functions as logout button
                // connected to /fragments/header.html through Thymeleaf (th:href="@{/logout}")
                    .logoutSuccessUrl("/login")
                // ^^ landing page after successful logout
                .and()
                .rememberMe()
                    .tokenValiditySeconds(120)
                    .key("cydeo")   // can be anything
                    .userDetailsService(securityService) // captures Security User in system
                .and()
                .build();
    }

}
