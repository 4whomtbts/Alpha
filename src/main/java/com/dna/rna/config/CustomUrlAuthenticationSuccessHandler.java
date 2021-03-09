package com.dna.rna.config;

import com.dna.rna.security.MainUserDetails;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Setter
public class CustomUrlAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomUrlAuthenticationSuccessHandler.class);

    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication authentication)
        throws ServletException, IOException {
        SavedRequest savedRequest = requestCache.getRequest(req, res);
        if (savedRequest != null) {
            requestCache.removeRequest(req, res);
            clearAuthenticationAttributes(req);
        }
        String accept = req.getHeader("accept");
        MainUserDetails securityUser = null;
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            Object principal = SecurityContextHolder.getContext()
                                                    .getAuthentication()
                                                    .getPrincipal();
            if (principal != null && principal instanceof UserDetails) {
                securityUser = (MainUserDetails) principal;
            }
        }

        if (accept == null || accept.matches(".*application/json.*") == false) {
            System.out.println("goto main");
        }

        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        MediaType jsonMimeType = MediaType.APPLICATION_JSON;
        System.out.println("well");
    }



}

