package com.group.pet_service.service;

import com.group.pet_service.dto.DataMailDTO;import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    public void sendHtmlMail(DataMailDTO dataMail) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

        // Build the HTML content
        String htmlContent = buildHtmlContent(dataMail.getProps());


        helper.setTo(dataMail.getTo());
        helper.setSubject(dataMail.getSubject());
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    private String buildHtmlContent(Map<String, Object> props) {
        // Tạo nội dung HTML dựa trên props
        // Tạo nội dung HTML dựa trên props
        String firstName = (String) props.get("firstName");
        String lastName = (String) props.get("lastName");
        String code = (String) props.get("code");

        StringBuilder html = new StringBuilder();
        html.append("<html>");
        html.append("<head>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; background-color: #f9f9f9; margin: 0; padding: 0; }");
        html.append(".container { background-color: #ffffff; padding: 20px; margin: 20px auto; max-width: 600px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }");
        html.append("h1 { color: #333333; }");
        html.append("p { font-size: 16px; color: #555555; }");
        html.append(".code { font-size: 24px; color: #007BFF; font-weight: bold; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class='container'>");
        html.append("<h1>Email Verification</h1>");
        html.append("<p>Hello ").append(firstName).append(" ").append(lastName).append(",</p>");
        html.append("<p>Thank you for signing up. Please use the verification code below to complete your registration:</p>");
        html.append("<p class='code'>").append(code).append("</p>");
        html.append("<p>This code will expire in 24 hours.</p>");
        html.append("<p>If you did not sign up for this account, please ignore this email.</p>");
        html.append("</div>");
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }
}