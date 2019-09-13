package com.aion.dashboard.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${com.aion.dashboard.configs.SecurityConfiguration.password}")
    public String password;
    @Value("${com.aion.dashboard.configs.SecurityConfiguration.user}")
    public String admin;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/dashboard/**", "/v2/dashboard/**")
                .permitAll()
                .antMatchers("/v2/monitor/reorgs").hasRole("ADMIN")
                .and()
                .csrf()
                .disable();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService(){

        UserDetails user = User.withUsername(admin).password(password).roles("USER", "ADMIN").passwordEncoder(
            PasswordEncoderFactories.createDelegatingPasswordEncoder()::encode).build();
        return new InMemoryUserDetailsManager(user);
    }
}
