package com.holiday.touristadviceservice.security;

import com.holiday.touristadviceservice.security.jwt.JwtSecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String CITIES_ENDPOINT = "/cities";
    private static final String USER_INFO_ENDPOINT = "/users/info";
    private static final String SIGN_UP_ENDPOINT = "/users/sign-up";
    private static final String USERS_LOGOUT_ENDPOINT = "/users/logout";
    private static final String LOGIN_ENDPOINT = "/login**";
    private static final String OAUTH_AUTHORIZATION_ENDPOINT = "/login/oauth2/authorization/*";

    private static final String H2_ENDPOINT = "/h2/**";
    private static final String SWAGGER_API_DOCS = "/v2/api-docs";
    private static final String SWAGGER_UI = "/swagger-ui.html";
    private static final String SWAGGER_WEBJARS = "/webjars/**";
    private static final String SWAGGER_CONFIGURATION_UI = "/configuration/ui";
    private static final String SWAGGER_RESOURCES = "/swagger-resources/**";
    private static final String SWAGGER_CONFIGURATION = "/configuration/**";

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AppUserDetailsService userDetailsService;

    @Autowired
    private JwtSecurityConfig jwtSecurityConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers(CITIES_ENDPOINT, USER_INFO_ENDPOINT).authenticated()
                .antMatchers(H2_ENDPOINT, USERS_LOGOUT_ENDPOINT, SIGN_UP_ENDPOINT, LOGIN_ENDPOINT, OAUTH_AUTHORIZATION_ENDPOINT).permitAll()
                .antMatchers(SWAGGER_UI, SWAGGER_WEBJARS, SWAGGER_API_DOCS, SWAGGER_CONFIGURATION, SWAGGER_CONFIGURATION_UI, SWAGGER_RESOURCES).permitAll()
                .anyRequest().authenticated();
        // with oauth2ClientAuthenticationProcessingFilter
        // throw authenticationEntryPoint instead of redirect to sign-in in google
        // if we comment this line we will be redirect for each protected endpoint
        // if we want that our user can sign-in in google through special endpoint
        // without redirect to google for each protected endpoint
        // we must set security.oauth2.sso.loginPath=/login/oauth2/**
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);

        // with oauth2 add problem ERR_TOO_MANY_REDIRECTS
        // with oauth2 and authenticationEntryPoint - Unauthorized Error
        // policy = NEVER oauth2 work with cookie
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);

        //may be create JwtSecurityConfig for work with jwt only
        //may be create GenericSecurityConfig with generic option and endpoint for inject another configuration for example jwt,oauth
        http.headers().frameOptions().disable(); //for download some file for correct work h2 console

        http.apply(jwtSecurityConfig); // add jwt security configuration

        http.oauth2Login().authorizationEndpoint().baseUri(OAUTH_AUTHORIZATION_ENDPOINT)
                .and()
                .defaultSuccessUrl(USER_INFO_ENDPOINT, true);
        http.logout().logoutSuccessUrl(USERS_LOGOUT_ENDPOINT).permitAll();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
