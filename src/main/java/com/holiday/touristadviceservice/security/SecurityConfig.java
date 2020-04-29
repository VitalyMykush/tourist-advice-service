package com.holiday.touristadviceservice.security;

import com.holiday.touristadviceservice.security.jwt.JwtAuthenticationFilter;
import com.holiday.touristadviceservice.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private static final String CITIES_ENDPOINT = "/cities";
    private static final String H2_ENDPOINT = "/h2/**";
    private static final String AUTH_ENDPOINT = "/auth/**";
    private static final String USER_INFO_ENDPOINT = "/me";
    private static final String HOME_ENDPOINT = "/home";



    @Autowired
    private final JwtService jwtTokenService;
    @Autowired
    private final JwtAuthenticationFilter authenticationFilter;
    @Autowired
    private final AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    AppUserDetailsService appUserDetailsService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable().authorizeRequests()
                .antMatchers(CITIES_ENDPOINT, USER_INFO_ENDPOINT).authenticated()
                .antMatchers(AUTH_ENDPOINT, H2_ENDPOINT,HOME_ENDPOINT).permitAll()
                .anyRequest().authenticated()
                .and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
