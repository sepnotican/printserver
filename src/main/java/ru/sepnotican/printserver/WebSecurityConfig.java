package ru.sepnotican.printserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled=true, prePostEnabled=true)

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyAppUserDetailsService myAppUserDetailsService;
    @Autowired
    private BasicAuthenticationEntryPoint appAuthenticationEntryPoint;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/echo/**")
                .permitAll()
//                .hasAnyRole("ADMIN","USER")
                .and().httpBasic().realmName("MY APP REALM")
                .authenticationEntryPoint(appAuthenticationEntryPoint);
    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        auth.userDetailsService(myAppUserDetailsService).passwordEncoder(passwordEncoder);
    }


    @Bean
    BasicAuthenticationFilter basicAuthFilter(AuthenticationManager authenticationManager, BasicAuthenticationEntryPoint basicAuthEntryPoint) {
        return new BasicAuthenticationFilter(authenticationManager, basicAuthEntryPoint());
    }
    @Bean
    BasicAuthenticationEntryPoint basicAuthEntryPoint() {
        BasicAuthenticationEntryPoint bauth = new BasicAuthenticationEntryPoint();
        bauth.setRealmName("MY APP REALM");
        return bauth;
    }


}
