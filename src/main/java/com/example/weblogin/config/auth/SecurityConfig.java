package com.example.weblogin.config.auth;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.weblogin.service.JwtTokenProvider;

@EnableWebSecurity // 해당 파일로 시큐리티 활성화, springSecurityFilterChain가 자동으로 포함
@Configuration // IoC 등록
public class SecurityConfig {
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

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf((csrf) -> csrf.disable());

		http.authorizeHttpRequests((authorizeHttpRequests) ->
			authorizeHttpRequests
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정(static에는 접근 허용)
				.requestMatchers("**/seller/**").hasRole("SELLER")   // 해당 URL은 ADMIN 권한을 가진 사람만 접근 가능
				.requestMatchers("**/user/**").authenticated()   // 해당 URL은 로그인한 이용자만 접근 가능
				.requestMatchers("/members/signup", "/members/signin",
					"/common/**").permitAll()   // 해당 URL은 로그인 없이 인증가능
				.anyRequest().authenticated()
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

		//JWTAuthenticationFilter 추가
		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(principalDetailsService).passwordEncoder(passwordEncoder());
	}
}
