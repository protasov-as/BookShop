package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.security.jwt.CustomLogoutHandler;
import com.example.MyBookShopApp.security.jwt.JWTRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final BookstoreUserDetailsService bookstoreUserDetailsService;
    private final JWTRequestFilter filter;
    private final CustomOAuth2UserService oauthUserService;
    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final CustomLogoutHandler customLogoutHandler;

    @Autowired
    public SecurityConfig(BookstoreUserDetailsService bookstoreUserDetailsService, JWTRequestFilter filter, CustomOAuth2UserService oauthUserService, CustomAuthenticationProvider customAuthenticationProvider, CustomLogoutHandler customLogoutHandler) {
        this.bookstoreUserDetailsService = bookstoreUserDetailsService;
        this.filter = filter;
        this.oauthUserService = oauthUserService;
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.customLogoutHandler = customLogoutHandler;
    }

    @Bean
    PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.
//                userDetailsService(bookstoreUserDetailsService)
//                .passwordEncoder(getPasswordEncoder());
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/my","/profile").authenticated() //.hasRole("USER")
                .antMatchers("/**", "/login", "/oauth/**").permitAll()
                .and().formLogin()
                .loginPage("/signin").failureForwardUrl("/signin")
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/signin").deleteCookies("token").addLogoutHandler(customLogoutHandler)
                .and().oauth2Login().loginPage("/signin").userInfoEndpoint().userService(oauthUserService) //.loginPage("/signin").defaultSuccessUrl("/oauth2LoginSuccess")
                .and()
                .successHandler((request, response, authentication) -> {
                    CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
                    bookstoreUserDetailsService.processOAuthPostLogin(oauthUser.getEmail(), oauthUser.getName());
                    response.sendRedirect("/my");
                });

//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
