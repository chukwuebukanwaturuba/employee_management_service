package com.ebuka.employeemanagementsysytem.configuration.rabbitmq;

import com.ebuka.employeemanagementsysytem.model.dto.request.MailRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailListener {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void sendEmail(String message) {
        try {
            MailRequest mailRequest = objectMapper.readValue(message, MailRequest.class);

            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(mailRequest.getTo());
            mail.setSubject(mailRequest.getSubject());
            mail.setText(mailRequest.getBody());
            javaMailSender.send(mail);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
