package com.shiromi.ashiura.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class WebAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());

        if(accessDeniedException != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication != null &&
                    ((User) authentication.getPrincipal()).getAuthorities().contains(new SimpleGrantedAuthority("ROLE_Y"))) {
                request.setAttribute("msg","you do not have access_Y");
                request.setAttribute("nextPage","/");
            }
            if (authentication != null &&
                    ((User) authentication.getPrincipal()).getAuthorities().contains(new SimpleGrantedAuthority("ROLE_N"))) {
                request.setAttribute("msg","you do not have access_N");
                request.setAttribute("nextPage","/");
            }
//            if (authentication != null &&
//                    ((User) authentication.getPrincipal()).getAuthorities().contains(new SimpleGrantedAuthority("ROLE_"))) {
//                request.setAttribute("msg", "you do not have access");
//                request.setAttribute("nextPage", "/auth/loginForm");
//            }
            else {
                request.setAttribute("msg","you shall not pass");
                request.setAttribute("nextPage","/auth/loginForm");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                SecurityContextHolder.clearContext();
            }

        } else {
            log.info(accessDeniedException.getClass().getCanonicalName());
        }
        request.getRequestDispatcher("/err/denied-page").forward(request, response);
    }
}
