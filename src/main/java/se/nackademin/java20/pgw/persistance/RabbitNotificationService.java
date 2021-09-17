package se.nackademin.java20.pgw.persistance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import se.nackademin.java20.pgw.domain.Payment;
import se.nackademin.java20.pgw.domain.PaymentNotificationService;

import java.util.HashMap;
import java.util.Map;

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
        //String object = objectMapper.writeValueAsString(new PaymentMessageDto(payment.getReference(), "" + payment.getId(), payment.getStatus()));

//            template.convertAndSend("payments-exchange", payment.getReference(), object);
        //kalla på url:en

        RestTemplate restTemplate = new RestTemplate();

        //serialierar
        Map<String, Object> map = new HashMap<>();
        map.put("reference",payment.getReference());
        map.put("status",payment.getStatus());

        LOG.info("Sending {}", map);

        //vi vill ta bort "add"
        ResponseEntity<Payment> response = restTemplate
                .postForEntity("/payment", map, Payment.class);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());

    }
}
