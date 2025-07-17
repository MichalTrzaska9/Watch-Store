package com.example.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class UrlUtil {

    public static String generateUrl(HttpServletRequest request) {

        String url = request.getRequestURL().toString();

        return url.replace(request.getServletPath(), "");
    }
}
