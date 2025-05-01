package com.spring.twofactor.service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
@Service
public class OtpService {
    private final Map<String, String> otpStore = new ConcurrentHashMap<>();

    public String generateOtp(String username) {
        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        otpStore.put(username, otp);
        return otp;
    }

    public boolean verifyOtp(String username, String otp) {
        return otp.equals(otpStore.get(username));
    }

    public void clearOtp(String username) {
        otpStore.remove(username);
    }
}
