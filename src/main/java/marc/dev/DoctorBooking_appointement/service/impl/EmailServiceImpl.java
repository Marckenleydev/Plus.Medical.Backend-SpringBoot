package marc.dev.DoctorBooking_appointement.service.impl;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marc.dev.DoctorBooking_appointement.exception.ApiException;
import marc.dev.DoctorBooking_appointement.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import org.thymeleaf.TemplateEngine;

import java.time.LocalDateTime;
import java.util.Map;

import static marc.dev.DoctorBooking_appointement.utils.EmailUtils.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    private static final String NEW_USER_ACCOUNT_VERIFICATION = "New User Account Verification";
    private static final String PASSWORD_RESET_REQUEST = "Reset Password Request";
    private static final String APPOINTMENT_CONFIRMATION = "Appointment Confirmation Email";
    private final JavaMailSender sender;
    private final TemplateEngine templateEngine;
    private String EMAIL_TEMPLATE ="emailtemplate";
    private static final String UTF_8_ENCODING = "UTF-8";
    @Value("${spring.mail.verify.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendHtmlConfirmationAppointmentMail(String patientName, String doctorName, LocalDateTime appointmentDateTime, String appointmentCode, String to) {
        try{
            Context context = new Context();
            context.setVariables(Map.of(
                    "patientName",patientName,
                    "doctorName",doctorName,
                    "appointmentDateTime", appointmentDateTime,
                    "appointmentCode", appointmentCode));
            String text = templateEngine.process(EMAIL_TEMPLATE, context);
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(APPOINTMENT_CONFIRMATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(text, true);


            sender.send(message);
        }catch(Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());

        }
    }

    public void sendPatientNewAccountEmail(String name, String email, String token) {
        try {
            var message = new SimpleMailMessage();
            message.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setText(getPatientEmailMessage(name, host, token));
            sender.send(message);
        } catch (Exception exception) {
            log.error("Mail server connection failed. Failed messages: {}", exception.getMessage());
            throw new ApiException("Unable to send email");
        }
    }
    @Override
    @Async
    public void sendPatientPasswordResetEmail(String Firstname, String email, String token) {
        try {
            var message = new SimpleMailMessage();
            message.setSubject(PASSWORD_RESET_REQUEST);
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setText(getPatientResetPasswordMessage(Firstname, host, token));
            sender.send(message);
        }catch(Exception exception){
            log.error(exception.getMessage());
            throw new ApiException("Unable to send email");
        }

    }




    public void sendStaffNewAccountEmail(String name, String email, String token) {
        try {
            var message = new SimpleMailMessage();
            message.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setText(getStaffEmailMessage(name, host, token));
            sender.send(message);
        } catch (Exception exception) {
            log.error("Mail server connection failed. Failed messages: {}", exception.getMessage());
            throw new ApiException("Unable to send email");
        }
    }
    @Override
    @Async
    public void sendStaffPasswordResetEmail(String Firstname, String email, String token) {
        try {
            var message = new SimpleMailMessage();
            message.setSubject(PASSWORD_RESET_REQUEST);
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setText(getStaffResetPasswordMessage(Firstname, host, token));
            sender.send(message);
        }catch(Exception exception){
            log.error(exception.getMessage());
            throw new ApiException("Unable to send email");
        }

    }

    public void sendDoctorNewAccountEmail(String name, String email, String token) {
        try {
            var message = new SimpleMailMessage();
            message.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setText(getDoctorEmailMessage(name, host, token));
            sender.send(message);
        } catch (Exception exception) {
            log.error("Mail server connection failed. Failed messages: {}", exception.getMessage());
            throw new ApiException("Unable to send email");
        }
    }
    @Override
    @Async
    public void sendDoctorPasswordResetEmail(String Firstname, String email, String token) {
        try {
            var message = new SimpleMailMessage();
            message.setSubject(PASSWORD_RESET_REQUEST);
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setText(getDoctorResetPasswordMessage(Firstname, host, token));
            sender.send(message);
        }catch(Exception exception){
            log.error(exception.getMessage());
            throw new ApiException("Unable to send email");
        }

    }


    private MimeMessage getMimeMessage() {
        return sender.createMimeMessage();
    }

}
















