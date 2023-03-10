package com.training.service.impl;

import com.training.entity.Ticket;
import com.training.entity.User;
import com.training.service.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private static final String MESSAGING_EXCEPTION_MSG = "Failed to send email - invalid recipient list.";
    private static final String INVALID_RECIPIENTS_MSG = "Failed to send email - invalid recipient list.";
    private static final String INCORRECT_SUBJECT_MSG = "Incorrect subject. Mail not sent.";

    private static final String NEW_TICKET_MAIL_TEMPLATE_NAME = "mail/newTicketMail";
    private static final String APPROVED_TICKET_MAIL_TEMPLATE_NAME = "mail/approvedTicketMail";
    private static final String DECLINED_TICKET_MAIL_TEMPLATE_NAME = "mail/declinedTicketMail";
    private static final String CANCELLED_BY_MANAGER_TICKET_MAIL_TEMPLATE_NAME = "mail/cancelledByManagerTicketMail";
    private static final String CANCELLED_BY_ENGINEER_TICKET_MAIL_TEMPLATE_NAME = "mail/cancelledByEngineerTicketMail";
    private static final String DONE_TICKET_MAIL_TEMPLATE_NAME = "mail/doneTicketMail";
    private static final String FEEDBACK_PROVIDED_MAIL_TEMPLATE_NAME = "mail/feedbackProvidedMail";

    private static final String NEW_TICKET_SUBJECT = "New ticket for approval";
    private static final String TICKET_APPROVED_SUBJECT = "Ticket was approved";
    private static final String TICKET_DECLINED_SUBJECT = "Ticket was declined";
    private static final String TICKET_CANCELLED_SUBJECT = "Ticket was cancelled";
    private static final String TICKET_DONE_SUBJECT = "Ticket was done";
    private static final String FEEDBACK_PROVIDED_SUBJECT = "Feedback was provided";

    private static final String RECIPIENT_CONTEXT_KEY = "recipient";
    private static final String TICKET_CONTEXT_KEY = "ticket";

    private final JavaMailSender mailSender;
    private final TemplateEngine htmlTemplateEngine;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Async
    @Override
    public void sendTicketHandlingEmail(List<User> recipients, Ticket ticket, String subject) {
        if (recipients == null || recipients.isEmpty()) {
            throw new IllegalArgumentException(INVALID_RECIPIENTS_MSG);
        }

        try {
            for (var recipient : recipients) {
                var mimeMessage = mailSender.createMimeMessage();
                var messageHelper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());

                messageHelper.setTo(recipient.getEmail());
                messageHelper.setFrom(emailFrom);
                messageHelper.setSubject(subject);

                var context = new Context();
                context.setVariable(RECIPIENT_CONTEXT_KEY, recipient);
                context.setVariable(TICKET_CONTEXT_KEY, ticket);

                var htmlContent = chooseTemplate(subject, context, recipients.size());
                messageHelper.setText(htmlContent, true);

                mailSender.send(mimeMessage);
            }
        } catch (MessagingException ex) {
            throw new MailSendException(MESSAGING_EXCEPTION_MSG, ex);
        }
    }

    private String chooseTemplate(String subject, Context context, int recipientsNumber) {
        return switch (subject) {
            case NEW_TICKET_SUBJECT -> htmlTemplateEngine.process(NEW_TICKET_MAIL_TEMPLATE_NAME, context);
            case TICKET_APPROVED_SUBJECT -> htmlTemplateEngine.process(APPROVED_TICKET_MAIL_TEMPLATE_NAME, context);
            case TICKET_DECLINED_SUBJECT -> htmlTemplateEngine.process(DECLINED_TICKET_MAIL_TEMPLATE_NAME, context);
            case TICKET_CANCELLED_SUBJECT -> isOne(recipientsNumber)
                    ? htmlTemplateEngine.process(CANCELLED_BY_MANAGER_TICKET_MAIL_TEMPLATE_NAME, context)
                    : htmlTemplateEngine.process(CANCELLED_BY_ENGINEER_TICKET_MAIL_TEMPLATE_NAME, context);
            case TICKET_DONE_SUBJECT -> htmlTemplateEngine.process(DONE_TICKET_MAIL_TEMPLATE_NAME, context);
            case FEEDBACK_PROVIDED_SUBJECT -> htmlTemplateEngine.process(FEEDBACK_PROVIDED_MAIL_TEMPLATE_NAME, context);
            default -> throw new IllegalArgumentException(INCORRECT_SUBJECT_MSG);
        };
    }

    private boolean isOne(int value) {
        return value == 1;
    }
}
