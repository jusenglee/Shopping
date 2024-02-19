package com.example.weblogin.config.baseEntity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * BaseEntity에 데이터를 담기위한 구현체.
 */
@Configuration
@EnableJpaAuditing
public class Auditing {

	@Bean
	public AuditorAware<String> auditorProvider() {
		return new AuditorAwareImpl();
	}
}
