package com.example.currency.finance.controller;

import com.example.currency.finance.dto.LoginRequest;
import com.example.currency.finance.dto.SignupRequest;
import com.example.currency.finance.model.User;
import com.example.currency.finance.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse; // Import Response
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Import this
import org.springframework.security.core.context.SecurityContext; // Import this
import org.springframework.security.core.context.SecurityContextHolder; // Import this
import org.springframework.security.web.context.HttpSessionSecurityContextRepository; // Import this
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginRequest") LoginRequest loginRequest,
                        BindingResult result,
                        HttpServletRequest request,
                        HttpServletResponse response, // Add Response parameter
                        Model model) {
        if (result.hasErrors()) {
            return "login";
        }

        try {
            // 1. Verify credentials against DB
            User user = authService.authenticateUser(loginRequest);

            // 2. Set your custom Session Attribute (for your app logic)
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(3600);

            // 3. --- FIX: MANUALLY REGISTER WITH SPRING SECURITY ---
            // Create a token (Principal, Credentials, Authorities)
            // We pass 'user' as the principal and empty list for authorities (roles)
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());

            // Create a new SecurityContext
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authToken);
            SecurityContextHolder.setContext(securityContext);

            // Save this context to the session so Spring Security remembers it on the next page load
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

            return "redirect:/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/signup")
    public String showSignupPage(Model model) {
        model.addAttribute("signupRequest", new SignupRequest());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute("signupRequest") SignupRequest signupRequest,
                         BindingResult result,
                         Model model) {
        if (result.hasErrors()) {
            return "signup";
        }

        try {
            authService.registerUser(signupRequest);
            return "redirect:/login?success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext(); // Clear Spring Security context
        return "index";
    }
}