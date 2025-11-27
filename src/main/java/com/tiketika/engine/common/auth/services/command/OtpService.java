package com.tiketika.engine.common.auth.services.command;

import com.tiketika.engine.common.auth.entities.Otp;
import com.tiketika.engine.common.auth.repositories.OtpRepository;
import com.tiketika.engine.common.auth.entities.User;
import com.tiketika.engine.common.auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {

    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    private final SmsService smsService;
    private final EmailService emailService;

    private static final long OTP_EXPIRY_MINUTES = 3;
    private static final int OTP_LENGTH = 6;

    /**
     * Generate OTP and send via SMS + Email (for registration verification)
     */
    public Otp generateAndSendOtp(User user) {
        String otpCode = generateRandomOtp();

        Otp otp = otpRepository.findByUser(user).orElse(new Otp());
        otp.setUser(user);
        otp.setCode(otpCode);
        otp.setExpiryTime(System.currentTimeMillis() + OTP_EXPIRY_MINUTES * 60 * 1000);
        otp.setVerified(false);
        otpRepository.save(otp);

        String message = "Your Tiketika verification OTP is " + otpCode + ". Valid for 3 minutes.";

        if (user.getPhone() != null && !user.getPhone().isBlank()) {
            try {
                smsService.sendSms(user.getPhone(), message);
                log.info("Verification OTP sent via SMS to: {}", user.getPhone());
            } catch (Exception e) {
                log.error("Failed to send SMS to {}: {}", user.getPhone(), e.getMessage());
            }
        }

        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            try {
                emailService.sendEmail(user.getEmail(), "Verify your Tiketika Account", message);
                log.info("Verification OTP sent via EMAIL to: {}", user.getEmail());
            } catch (Exception e) {
                log.error("Failed to send email to {}: {}", user.getEmail(), e.getMessage(), e);
            }
        }

        return otp;
    }

    /**
     * Verify OTP code for registration
     */
    public boolean verifyOtp(User user, String code) {
        return otpRepository.findByUserAndCode(user, code)
                .filter(otp -> !otp.isVerified())
                .filter(otp -> otp.getExpiryTime() >= System.currentTimeMillis())
                .map(otp -> {
                    otp.setVerified(true);
                    otpRepository.save(otp);

                    // Mark user as email verified
                    user.setIsEmailVerified(true);
                    userRepository.save(user);

                    log.info("OTP verified and email marked as verified for user: {}", user.getEmail());
                    return true;
                })
                .orElseGet(() -> {
                    log.warn("Invalid or expired OTP for user: {}", user.getEmail());
                    return false;
                });
    }

    /**
     * Generate OTP and send via Email only (if needed)
     *
     * @return
     */
    public Otp generateAndSendOtpEmailOnly(User user) {
        String otpCode = generateRandomOtp();

        Otp otp = otpRepository.findByUser(user).orElse(new Otp());
        otp.setUser(user);
        otp.setCode(otpCode);
        otp.setExpiryTime(System.currentTimeMillis() + OTP_EXPIRY_MINUTES * 60 * 1000);
        otp.setVerified(false);
        otpRepository.save(otp);

        String message = "Your Tiketika verification OTP is " + otpCode + ". Valid for 3 minutes.";

        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            try {
                emailService.sendEmail(
                        user.getEmail(),
                        "Verify your Tiketika Account",
                        message
                );
                log.info("Verification OTP sent via EMAIL ONLY to {}", user.getEmail());
            } catch (Exception e) {
                log.error("Failed to send OTP email to: {}", user.getEmail(), e);
                throw new RuntimeException("Failed to send OTP via email", e);
            }
        } else {
            throw new RuntimeException("User has no email address");
        }

        return otp;
    }


    /**
     * Generate secure 6-digit OTP using ThreadLocalRandom
     */
    private String generateRandomOtp() {
        int otp = ThreadLocalRandom.current().nextInt(1_000_000);
        return String.format("%0" + OTP_LENGTH + "d", otp); // Ensures 6 digits (e.g., 001234)
    }
}
