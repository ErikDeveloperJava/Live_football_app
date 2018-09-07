package net.livefootball.config.security;

import net.livefootball.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("USD")
    private UserDetailsService userDetailsService;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/","/error","/leagues")
                .permitAll()
                .antMatchers("/login","/register")
                .anonymous()
                .antMatchers("/admin","/admin/**")
                .hasAuthority(UserRole.ADMIN.name())
                .antMatchers("/league/*","/clubs","/league/*/clubs","/club/*",
                        "/club/*/players","/matches/*","/news","/news/*","/gallery")
                .hasAnyAuthority(UserRole.USER.name(),UserRole.ROLE_ANONYMOUS.name())
                .antMatchers(HttpMethod.GET,"/news/comment/*/page/*")
                .hasAnyAuthority(UserRole.USER.name(),UserRole.ROLE_ANONYMOUS.name())
                .antMatchers("/news/comment","/news/comment/**")
                .hasAuthority(UserRole.USER.name())
        .and()
                .formLogin()
                .loginPage("/")
                .loginProcessingUrl("/login")
                .failureUrl("/login/error")
                .defaultSuccessUrl("/login/success")
        .and()
                .rememberMe()
                .rememberMeCookieName("RM")
                .rememberMeParameter("remember-me")
                .tokenValiditySeconds(200000000)
                .userDetailsService(userDetailsService)
        .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
        .and()
                .logout()
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("RM")
        .and()
                .csrf()
                .disable();
    }
}
