package com.sokpulee.crescendo.domain.dm.controller;

import com.sokpulee.crescendo.domain.dm.dto.request.MessageRequest;
import com.sokpulee.crescendo.domain.dm.dto.response.MessageResponse;
import com.sokpulee.crescendo.domain.dm.service.DMService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final DMService dmService;

    @MessageMapping("/message")
    public void send(MessageRequest message) throws Exception {

        MessageResponse messageResponse = dmService.saveMessage(message);

        simpMessagingTemplate.convertAndSend("/topic/messages/" + message.getRecipientId(), messageResponse);
        simpMessagingTemplate.convertAndSend("/topic/messages/" + message.getWriterId(), messageResponse);

    }
}
