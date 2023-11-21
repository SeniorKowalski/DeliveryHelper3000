package ru.kowalski.DeliveryHelper3000.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;

// Контроллер для определения стартовой страницы

@Controller
public class DefaultController {

    @RequestMapping(value = {"/default"}, method = RequestMethod.GET)
    public String defaultAfterLogin() {
        Collection<? extends GrantedAuthority> authorities;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        authorities = auth.getAuthorities();
        String myRole = authorities.toArray()[0].toString();
        String admin = "ROLE_ADMIN";
        if (myRole.equals(admin)) {
            return "redirect:/admin/";
        }
        return "redirect:/partner/";
    }
}
