package com.example.InteriorsECM.controller;

import com.example.InteriorsECM.repository.UserRepository;
import com.example.InteriorsECM.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.InteriorsECM.model.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.SecureRandom;

@Controller
public class PasswordController {
    @Autowired
    public UserService userService;

    @Autowired
    private JavaMailSender mailSender;

    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam(name = "email", required = false) String email,
                                                 RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(email);
        if (user == null) {
            redirectAttributes.addFlashAttribute("fail", "Không tìm thấy email");
            return "redirect:/forgot-password";
        }

        String tempPassword = generateTemporaryPassword(12);
        passwordEncoder = new BCryptPasswordEncoder(12);
        String encodedPassword = passwordEncoder.encode(tempPassword);


        user.setPassword_hash(encodedPassword);
        userService.applyChanged(user);

        try {
            sendEmail(user.getEmail(), tempPassword);
            redirectAttributes.addFlashAttribute("success", "Mật khẩu tạm thời đã được gửi đến email của bạn.");
            return "redirect:/forgot-password";
        } catch (MessagingException e) {
            redirectAttributes.addFlashAttribute("fail", "Lỗi trong quá trình gửi mail.");
            return "redirect:/forgot-password";
        }
    }

    private String generateTemporaryPassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder tempPassword = new StringBuilder();
        for (int i = 0; i < length; i++) {
            tempPassword.append(characters.charAt(random.nextInt(characters.length())));
        }
        return tempPassword.toString();
    }

    private void sendEmail(String toEmail, String tempPassword) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject("Khôi phục mật khẩu - InteriorsECM");
        helper.setText(
                "<h2>Khôi phục mật khẩu</h2>" +
                        "<p>Xin chào,</p>" +
                        "<p>Bạn đã yêu cầu khôi phục mật khẩu cho tài khoản của mình.</p>" +
                        "<p>Mật khẩu tạm thời của bạn là: <strong>" + tempPassword + "</strong></p>" +
                        "<p>Vui lòng đăng nhập bằng mật khẩu này và đổi mật khẩu mới trong phần cài đặt tài khoản.</p>" +
                        "<p>Nếu bạn không yêu cầu khôi phục mật khẩu, vui lòng bỏ qua email này.</p>" +
                        "<p>Trân trọng,<br>Đội ngũ InteriorsECM</p>",
                true
        );

        mailSender.send(message);
    }
}