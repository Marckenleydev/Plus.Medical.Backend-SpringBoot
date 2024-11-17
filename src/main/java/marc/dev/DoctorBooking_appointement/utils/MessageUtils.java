package marc.dev.DoctorBooking_appointement.utils;

import marc.dev.DoctorBooking_appointement.entity.MessageEntity;

import java.util.UUID;

public class MessageUtils {
    public static MessageEntity createMessageEntity(String email, String subject, String message) {

        return MessageEntity.builder()
                .feedbackId(UUID.randomUUID().toString())
                .email(email)
                .subject(subject)
                .message(message)
                .build();

    }
}
