package org.zirota.islab1.utilty;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.zirota.islab1.dto.PersonEvent;

@Configuration
public class PersonChangeListener {
    private final SimpMessagingTemplate messagingTemplate;
    public PersonChangeListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(PersonEvent event) {
        messagingTemplate.convertAndSend("/topic/person", event);
    }
}
