package com.example.weblogin.config;

import com.example.weblogin.config.auth.PrincipalDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity // 해당 파일로 시큐리티 활성화, springSecurityFilterChain가 자동으로 포함
@Configuration // IoC 등록
public class SecurityConfig extends WebSecurityConfigurerAdapter  {
    @Autowired
    PrincipalDetailsService principalDetailsService;


    @Bean
    public BCryptPasswordEncoder encoder() {
        // DB 패스워드 암호화
        return new BCryptPasswordEncoder();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http); // 이 코드 삭제하면 기존 시큐리티가 가진 모든 기능 비활성화
//        http.csrf().disable(); // csrf 토큰 비활성화 코드

        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());// csrf 토큰 활성화 코드
        http.authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").authenticated()
                .antMatchers("/members/signup",
                        "/members/signin", "/css/**", "/js/**", "/images/**","/members/new").permitAll()
                .anyRequest().permitAll()
                .and()


                .formLogin()
                .loginPage("/members/signin")
                .defaultSuccessUrl("/main")
                .usernameParameter("email")
                .failureHandler(new CustomAuthenticationFailureHandler())
                .permitAll()
                .and()


                .logout()
                .logoutUrl("/members/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/main")


                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(principalDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
