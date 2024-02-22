package com.example.weblogin.config.auth;

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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.weblogin.service.JwtTokenProvider;

@EnableWebSecurity // 해당 파일로 시큐리티 활성화, springSecurityFilterChain가 자동으로 포함
@Configuration // IoC 등록
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final JwtTokenProvider tokenProvider;
	private final PrincipalDetailsService principalDetailsService;

	public SecurityConfig(JwtTokenProvider tokenProvider, PrincipalDetailsService principalDetailsService) {
		this.tokenProvider = tokenProvider;
		this.principalDetailsService = principalDetailsService;
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter(tokenProvider, principalDetailsService);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf()
			.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
			.and()
			.authorizeRequests()
			.antMatchers("/admin/**")
			.hasRole("ADMIN")
			.antMatchers("/user/**")
			.authenticated()
			.antMatchers("/members/signup", "/members/signin", "/css/**", "/js/**", "/images/**", "/members/new")
			.permitAll()
			.anyRequest()
			.permitAll()
			.and()
			.formLogin()
			.loginPage("/members/signin")
			.usernameParameter("email")
			.successHandler(new CustomAuthenticationSuccessHandler(tokenProvider)) // 인증 성공 핸들러 등록
			.failureHandler(new CustomAuthenticationFailureHandler())
			.and()
			.logout()
			.logoutUrl("/members/logout")
			.logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
			.logoutSuccessUrl("/")
			.invalidateHttpSession(true)
			.deleteCookies("JSESSIONID")
			.and()
			.exceptionHandling()
			.accessDeniedPage("/access-denied");

		// JWTAuthenticationFilter 추가
		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(principalDetailsService).passwordEncoder(passwordEncoder());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}
}
