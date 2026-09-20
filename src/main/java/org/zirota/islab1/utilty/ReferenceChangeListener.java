package org.zirota.islab1.utilty;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import org.zirota.islab1.dto.CoordinatesEvent;
import org.zirota.islab1.dto.LocationEvent;

@Component
public class ReferenceChangeListener {

    private final SimpMessagingTemplate messagingTemplate;

    public ReferenceChangeListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void coordinatesChanged(CoordinatesEvent event) {
        messagingTemplate.convertAndSend("/topic/coordinates", event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void locationChanged(LocationEvent event) {
        messagingTemplate.convertAndSend("/topic/locations", event);
    }
}