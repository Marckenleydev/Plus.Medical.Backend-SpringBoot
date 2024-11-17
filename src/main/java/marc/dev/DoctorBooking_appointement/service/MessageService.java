package marc.dev.DoctorBooking_appointement.service;

import marc.dev.DoctorBooking_appointement.entity.MessageEntity;
import org.springframework.data.domain.Page;

public interface MessageService {
    void createMessage(String email, String subject, String message);
    Page<MessageEntity> getFeedbacks(int page, int size);


}
