package com.euroloans.eindopdracht.config;

import com.euroloans.eindopdracht.Filter.JwtRequestFilter;
import com.euroloans.eindopdracht.repository.UserRepository;
import com.euroloans.eindopdracht.service.JwtService;
import com.euroloans.eindopdracht.service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public SecurityConfig(JwtService service, UserRepository userRepos) {
        this.jwtService = service;
        this.userRepository = userRepos;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, PasswordEncoder encoder, UserDetailsService udService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(udService)
                .passwordEncoder(encoder)
                .and()
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new MyUserDetailsService(this.userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .authorizeHttpRequests()
                //User Endpoints
                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                .requestMatchers(HttpMethod.GET, "/username").hasRole("EMPLOYEE")
                .requestMatchers(HttpMethod.GET, "/users").hasRole("EMPLOYEE")
                //Role Endpoint
                .requestMatchers("/roles").hasRole("EMPLOYEE")
                //Authentication Endpoint
                .requestMatchers(HttpMethod.POST, "/auth").permitAll()
                //LoanRequests Endpoints
                .requestMatchers(HttpMethod.POST, "/loanRequests").hasRole("BORROWER")
                .requestMatchers(HttpMethod.PUT, "/loanRequests").hasAnyRole("EMPLOYEE", "BORROWER")
                .requestMatchers(HttpMethod.DELETE, "/loanRequests").hasRole("BORROWER")
                .requestMatchers(HttpMethod.GET, "/loanRequests").permitAll()
                .requestMatchers(HttpMethod.GET, "/loanRequests/{id}").permitAll()
                //Loans Endpoints
                .requestMatchers(HttpMethod.POST, "/loans").hasRole("EMPLOYEE")
                .requestMatchers(HttpMethod.GET, "/loan").hasAnyRole("BORROWER", "EMPLOYEE")
                .requestMatchers(HttpMethod.GET, "/loans").hasAnyRole("EMPLOYEE")
                //Payment Endpoints
                .requestMatchers(HttpMethod.GET, "/payments").hasAnyRole("EMPLOYEE")
                .requestMatchers(HttpMethod.GET, "/payments/{id}").hasAnyRole("EMPLOYEE")
                .requestMatchers(HttpMethod.POST, "/payments").hasAnyRole("BORROWER", "LENDER")
                .requestMatchers(HttpMethod.PUT, "/payments").hasRole("EMPLOYEE")
                //Investment Endpoints
                .requestMatchers(HttpMethod.POST, "/investments").hasRole("EMPLOYEE")
                .requestMatchers(HttpMethod.GET, "/investments").hasAnyRole("EMPLOYEE")
                .requestMatchers(HttpMethod.GET, "/investments/{id}").hasAnyRole("LENDER")
                //File Endpoint
                .requestMatchers(HttpMethod.POST, "/single/uploadDb").hasRole("BORROWER")
                .requestMatchers(HttpMethod.GET, "/downloadFromDb/{fileId}").hasRole("EMPLOYEE")

                .requestMatchers("/**").authenticated()
                .anyRequest().denyAll()
                .and()
                .addFilterBefore(new JwtRequestFilter(jwtService, userDetailsService()), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }
}
