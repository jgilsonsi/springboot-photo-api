package com.jjdev.photoapi;

import com.jjdev.photoapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.InternetAddress;

@Component
public class EmailConstructor {

    @Autowired
    private Environment env;

    @Autowired
    private TemplateEngine templateEngine;

    public MimeMessagePreparator constructNewUserEmail(User user, String password) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("password", password);
        String text = templateEngine.process("newUserEmailTemplate", context);
        return mimeMessage -> {
            MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
            email.setPriority(1);
            email.setTo(user.getEmail());
            email.setSubject("Welcome!");
            email.setText(text, true);
            email.setFrom(new InternetAddress(env.getProperty("support.email")));
        };
    }

    public MimeMessagePreparator constructUpdateUserProfileEmail(User user) {
        Context context = new Context();
        context.setVariable("user", user);
        String text = templateEngine.process("updateUserProfileEmailTemplate", context);
        return mimeMessage -> {
            MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
            email.setPriority(1);
            email.setTo(user.getEmail());
            email.setSubject("Profile Update");
            email.setText(text, true);
            email.setFrom(new InternetAddress(env.getProperty("support.email")));
        };
    }

    public MimeMessagePreparator constructResetPasswordEmail(User user, String password) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("password", password);
        String text = templateEngine.process("resetPasswordEmailTemplate", context);
        return mimeMessage -> {
            MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
            email.setPriority(1);
            email.setTo(user.getEmail());
            email.setSubject("New Password");
            email.setText(text, true);
            email.setFrom(new InternetAddress(env.getProperty("support.email")));
        };
    }
}
