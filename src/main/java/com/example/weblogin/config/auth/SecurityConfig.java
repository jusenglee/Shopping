package com.example.weblogin.config.auth;

import java.util.List;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
                http.csrf(csrf -> csrf.disable())
                        .authorizeHttpRequests(auth -> auth
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                .requestMatchers("/seller/**").hasRole("SELLER")
                                .requestMatchers("/user/**").authenticated()
                                .requestMatchers("/members/signup", "/members/signin", "/common/**").permitAll()
                                .anyRequest().authenticated()
                        )
                        .formLogin(form -> form
								.loginProcessingUrl("/members/signin")
                                .loginPage("/members/signin")
                                .usernameParameter("email")
                                .successHandler(new CustomAuthenticationSuccessHandler(tokenProvider))
                                .failureHandler(new CustomAuthenticationFailureHandler())
                        )
                        .logout(logout -> logout
                                .logoutUrl("/members/logout")
                                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                                .logoutSuccessUrl("/")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                        )
                        .exceptionHandling(ex -> ex.accessDeniedPage("/access-denied"));

                http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
                return http.build();
        }


        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                auth.userDetailsService(principalDetailsService).passwordEncoder(passwordEncoder());
        }

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();

		// Vue DevServer(8081)에서 오는 요청 허용
		config.setAllowedOrigins(List.of("http://localhost:8082"));
		// 모든 HTTP 메서드 허용
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		// 헤더 제한 없음
		config.setAllowedHeaders(List.of("*"));
		// 세션·JWT 쿠키가 필요하면
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		// 모든 엔드포인트에 동일 정책 적용
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
