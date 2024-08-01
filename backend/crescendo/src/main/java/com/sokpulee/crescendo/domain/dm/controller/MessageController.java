package com.sokpulee.crescendo.domain.dm.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;



//    @MessageMapping("/chat")
//    public void send(ChatDto chat) throws Exception {
//        log.info(chat.toString());
//
//        chat.setCreatedAt(LocalDateTime.now());
//        chatService.saveChat(chat);
//
//        UserInfoDto userInfoDto = userService.findByUserId(chat.getUserId());
//        chat.setNickName(userInfoDto.getNickname());
//        chat.setProfile(userInfoDto.getProfile());
//
//        simpMessagingTemplate.convertAndSend("/topic/messages/" + chat.getTripDetailId(), chat);
//    }
}
