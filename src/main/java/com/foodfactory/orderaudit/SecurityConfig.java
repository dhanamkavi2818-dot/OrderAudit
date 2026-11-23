
package com.foodfactory.orderaudit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth

                // LOGIN PAGE AND STYLES
                .requestMatchers(
                    "/login",
                    "/css/**",
                    "/js/**",
                    "/images/**"
                ).permitAll()

                // ADMIN ONLY: REPORTS AND MANAGEMENT
                .requestMatchers(
                    "/reports",
                    "/reports/**",
                    "/production-variance",
                    "/suppliers",
                    "/suppliers/**",
                    "/purchase-orders",
                    "/purchase-orders/**",
                    "/admin/**"
                ).hasRole("ADMIN")

                // SALES: ADMIN AND SALESMAN
                .requestMatchers(
                    "/orders",
                    "/orders/**",
                    "/customers",
                    "/customers/**",
                    "/invoices",
                    "/invoices/**",
                    "/payments",
                    "/payments/**"
                ).hasAnyRole("ADMIN", "SALESMAN")

                // PRODUCTION: ADMIN AND PRODUCTION
                
// PRODUCTION: ADMIN AND PRODUCTION
.requestMatchers(
    "/production-runs",
    "/production-runs/**",
    "/production",
    "/production/**",
    "/bom",
    "/bom/**",
    "/boms",
    "/boms/**",
    "/raw-material-stock",
    "/raw-material-stock/**",

"/stock",
"/stock/**",

    "/finished-goods-stock",
    "/finished-goods-stock/**",
    "/finished-stock",
"/finished-stock/**"
).hasAnyRole("ADMIN", "PRODUCTION")


                // ADMIN: PRODUCT AND RAW MATERIAL MANAGEMENT
                .requestMatchers(
                    "/products",
                    "/products/**",
                    "/raw-materials",
                    "/raw-materials/**"
                ).hasRole("ADMIN")

                // DASHBOARD
                .requestMatchers("/").authenticated()

                // ALL REMAINING URLS: ADMIN ONLY
                .anyRequest().hasRole("ADMIN")
            )

            
.formLogin(form -> form
    .loginPage("/login")
    .defaultSuccessUrl("/", true)
    .permitAll()
)


            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }

    // DEMONSTRATION USER ACCOUNTS

    @Bean
    public UserDetailsService userDetailsService(
            PasswordEncoder passwordEncoder) {

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("Admin@123"))
                .roles("ADMIN")
                .build();

        UserDetails salesman = User.builder()
                .username("salesman")
                .password(passwordEncoder.encode("Sales@123"))
                .roles("SALESMAN")
                .build();

        UserDetails production = User.builder()
                .username("production")
                .password(passwordEncoder.encode("Production@123"))
                .roles("PRODUCTION")
                .build();

        return new InMemoryUserDetailsManager(
                admin, salesman, production
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
