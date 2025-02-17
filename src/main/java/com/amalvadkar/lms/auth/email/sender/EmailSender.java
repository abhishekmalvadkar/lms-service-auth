package com.amalvadkar.lms.auth.email.sender;

import com.amalvadkar.lms.auth.ApplicationProperties;
import com.amalvadkar.lms.auth.email.dto.MailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Comment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailSender {

     private final JavaMailSender javaMailSender;
     private final SpringTemplateEngine templateEngine;
     private final ApplicationProperties appProps;

    @Async
    // TODO: Need to track email sent or not or any exception happen because emails are important
    public void sendInAsync(MailDto mailDTO) {
        try {
            if (emailSendIsDisbaled()){
                log.warn("Email will not be send because email send feature is disabled");
                return;
            }
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            mimeMessageHelper.setTo(mailDTO.toMail());
            mimeMessageHelper.setSubject(mailDTO.subject());

            Context context = new Context();
            context.setVariables(mailDTO.props());

            String emailBody = templateEngine.process(mailDTO.templateFileName(), context);

            mimeMessageHelper.setText(emailBody, true);

            javaMailSender.send(mimeMessage);
            log.info("Email sent successfully");

        } catch (MessagingException e) {
            throw new RuntimeException(String.format("Exception occurred while sending %s",
                    mailDTO.templateFileName()), e);
        }
    }

    private boolean emailSendIsDisbaled() {
        return !appProps.emailSendEnabled();
    }



}
