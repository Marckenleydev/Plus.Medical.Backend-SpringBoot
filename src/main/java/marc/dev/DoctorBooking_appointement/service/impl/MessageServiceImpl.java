package marc.dev.DoctorBooking_appointement.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marc.dev.DoctorBooking_appointement.entity.MessageEntity;
import marc.dev.DoctorBooking_appointement.repository.MessageRepository;
import marc.dev.DoctorBooking_appointement.service.MessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import static marc.dev.DoctorBooking_appointement.utils.MessageUtils.createMessageEntity;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;


    @Override
    public void createMessage(String email, String subject, String message) {
        messageRepository.save(createMessageEntity(email,subject,message));
    }

    @Override
    public Page<MessageEntity> getFeedbacks(int page, int size) {
        return messageRepository.findAll(PageRequest.of(page, size));
    }
}
