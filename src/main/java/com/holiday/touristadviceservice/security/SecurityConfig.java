package com.holiday.touristadviceservice.security;

import com.holiday.touristadviceservice.security.jwt.JwtAuthenticationFilter;
import com.holiday.touristadviceservice.security.jwt.JwtAuthorizationFilter;
import com.holiday.touristadviceservice.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String CITIES_ENDPOINT = "/cities";
    private static final String USER_INFO_ENDPOINT = "/users/info";
    private static final String SIGN_UP_ENDPOINT = "/users/sign-up";
    private static final String LOGIN_ENDPOINT = "/login";

    private static final String H2_ENDPOINT = "/h2/**";

    private static final String SWAGGER_API_DOCS="/v2/api-docs";
    private static final String SWAGGER_UI="/swagger-ui.html";
    private static final String SWAGGER_WEBJARS = "/webjars/**";
    private static final String SWAGGER_CONFIGURATION_UI = "/configuration/ui";
    private static final String SWAGGER_RESOURCES = "/swagger-resources/**";
    private static final String SWAGGER_CONFIGURATION = "/configuration/**";

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AppUserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers(CITIES_ENDPOINT,USER_INFO_ENDPOINT).authenticated()
                .antMatchers(H2_ENDPOINT, SIGN_UP_ENDPOINT,LOGIN_ENDPOINT).permitAll()
                .antMatchers(SWAGGER_UI,SWAGGER_WEBJARS,SWAGGER_API_DOCS,SWAGGER_CONFIGURATION,SWAGGER_CONFIGURATION_UI,SWAGGER_RESOURCES).permitAll()
                .anyRequest().authenticated()
                .and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.headers().frameOptions().disable(); //for h2 console
        http.addFilter(authenticationFilter());
        http.addFilter(authorizationFilter());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
    public JwtAuthenticationFilter authenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(jwtService,authenticationManager());
    }

    @Bean
    public JwtAuthorizationFilter authorizationFilter() throws Exception {
        return new JwtAuthorizationFilter(jwtService, authenticationManager());
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
