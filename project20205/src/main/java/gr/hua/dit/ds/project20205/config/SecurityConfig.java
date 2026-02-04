package gr.hua.dit.ds.project20205.config;


import gr.hua.dit.ds.project20205.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private UserService userService;

    private UserDetailsService userDetailsService;

    private BCryptPasswordEncoder passwordEncoder;

    public SecurityConfig(UserService userService, UserDetailsService userDetailsService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/home", "/register", "/saveUser", "/images/**", "/js/**", "/css/**").permitAll()
                        .requestMatchers("/admin/admin-dashboard/**", "/users/**").hasRole("ADMIN") // Μόνο για ADMIN
                        .requestMatchers("/applications/**", "/properties/add/**").hasAnyRole("ADMIN", "OWNER") // Για ADMIN & OWNER
                        .requestMatchers("/properties/apply/**").hasAnyRole("RENTAL", "OWNER")
                        .requestMatchers("/properties/**").hasAnyRole("RENTAL", "OWNER", "ADMIN") // Για RENTAL, OWNER, ADMIN
                        .anyRequest().authenticated() // Όλα τα άλλα χρειάζονται authentication
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll())
                .logout((logout) -> logout.permitAll());

        return http.build();
    }


}

