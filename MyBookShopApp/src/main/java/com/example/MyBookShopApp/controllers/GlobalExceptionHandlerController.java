package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.errs.EmptySearchException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.logging.Logger;

@ControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(EmptySearchException.class)
    public String handleEmptySearchException(EmptySearchException e, RedirectAttributes redirectAttributes){
        Logger.getLogger(this.getClass().getSimpleName()).warning(e.getLocalizedMessage());
        redirectAttributes.addFlashAttribute("searchError",e);
        return "redirect:/";
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public String handleUsernameNotFoundException(UsernameNotFoundException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("UsernameNotFoundError", ex);
        return "redirect:/signin";
    }

    @ExceptionHandler(AuthenticationException.class)
    public String handleAuthenticationException(AuthenticationException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("UsernameNotFoundError", ex);
        redirectAttributes.addFlashAttribute("IncorrectPasswordError", ex);
        return "redirect:/signin";
    }
}
