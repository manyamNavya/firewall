package com.firewall.app.config;
/*
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class FirewallSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${firewall.user.name}")
    private String defaultUsername;

    @Value("${firewall.user.password}")
    private String defaultPassword;

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        System.out.println(defaultUsername + " " + defaultPassword);
        UserDetails user = User.withUsername(defaultUsername)
                .password(defaultPassword)
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers( "/", "/js/**", "/css/**", "/images/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/")
                .permitAll()
                .defaultSuccessUrl("/dashboard/")
                .failureForwardUrl("/failed/")
                .and()
                .logout()
                .permitAll();
    }
}
*/