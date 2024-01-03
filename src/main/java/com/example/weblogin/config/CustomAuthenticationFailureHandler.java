package com.example.weblogin.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "아이디 또는 비밀번호가 잘못되었습니다.";

        if (exception.getMessage().equalsIgnoreCase("User is disabled")) {
            errorMessage = "계정이 비활성화되었습니다.";
        } else if (exception.getMessage().equalsIgnoreCase("User account has expired")) {
            errorMessage = "계정이 만료되었습니다.";
        }
        String encodedErrorMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.toString());
        setDefaultFailureUrl("/members/signin?error=true&exception=" + encodedErrorMessage);

        super.onAuthenticationFailure(request, response, exception);
    }
}