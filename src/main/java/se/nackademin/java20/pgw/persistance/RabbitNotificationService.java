package se.nackademin.java20.pgw.persistance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import se.nackademin.java20.pgw.domain.Payment;
import se.nackademin.java20.pgw.domain.PaymentNotificationService;

public class RabbitNotificationService implements PaymentNotificationService {
    private final static Logger LOG = LoggerFactory.getLogger(RabbitNotificationService.class);

    private final RabbitTemplate template;
    private final ObjectMapper objectMapper;

    public RabbitNotificationService(RabbitTemplate template, ObjectMapper objectMapper) {

        this.template = template;
        this.objectMapper = objectMapper;
    }

    @Override
    public void notifyPaid(Payment payment) {
        try {
            String object = objectMapper.writeValueAsString(new PaymentMessageDto(payment.getReference(), "" + payment.getId(), payment.getStatus()));
            LOG.info("Sending {}", object);
            template.convertAndSend("payments-exchange", payment.getReference(), object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
