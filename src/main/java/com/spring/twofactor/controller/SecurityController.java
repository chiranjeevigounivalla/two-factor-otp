package com.spring.twofactor.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.twofactor.service.EmailService;
import com.spring.twofactor.service.OtpService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SecurityController {
	
	@Autowired
	private OtpService otpService;
	
	@Autowired
	private EmailService emailService;

	    @GetMapping("/login")
	    public String login() {
	        return "login";
	    }

	    @GetMapping("/mfa")
	    public String showMfa(HttpServletRequest request, Model model) {
	        String username = request.getUserPrincipal().getName();
	        String otp = otpService.generateOtp(username);
	        System.out.println("Generated OTP for " + username + ": " + otp);
	        String email = username.equals("user") ? "rishikumar.Ilangovan@gmail.com" : null;
	        emailService.sendOtp(email, otp);
	 
	        return "mfa";
	    }

	    @PostMapping("/verify-otp")
	    public String verifyOtp(@RequestParam String otp, HttpServletRequest request) {
	        String username = request.getUserPrincipal().getName();
	        if (otpService.verifyOtp(username, otp)) {
	            request.getSession().setAttribute("MFA_AUTHENTICATED", true);
	            otpService.clearOtp(username);
	            return "redirect:/home";
	        }
	        return "redirect:/mfa?error";
	    }

	    @GetMapping("/home")
	    public String home() {
	        return "home";
	    }

}
