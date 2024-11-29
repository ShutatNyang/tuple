package com.ssafy.chat.controller;

import com.ssafy.chat.model.ChatDto;
import com.ssafy.chat.model.dto.request.SendMessageRequestDto;
import com.ssafy.chat.model.dto.response.SendMessageResponsetDto;
import com.ssafy.chat.model.service.ChatRoomService;
import com.ssafy.chat.model.service.ChatService;
import com.ssafy.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;

@Slf4j
@Controller
public class ChatWebSocketController {
    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    @Autowired
    public ChatWebSocketController(ChatService chatService, ChatRoomService chatRoomService) {
        this.chatService = chatService;
        this.chatRoomService = chatRoomService;
    }

    @MessageMapping("/chat.sendMessage/{roomId}")
    @SendTo("/topic/chatroom/{roomId}")
    public SendMessageResponsetDto sendMessage(@DestinationVariable int roomId, SendMessageRequestDto sendMessageRequestDto, @Header("Authorization") String token) {
        try {
            // JWT에서 senderId 추출
            String jwt = token.replace("Bearer ", "");
            String jwtEmail = JwtUtil.getEmailFromToken(jwt);
            int memberId = chatRoomService.getMemberIdByEmail(jwtEmail);

            ChatDto chatDto = new ChatDto();
            chatDto.setRoomId(roomId); // roomId 설정
            chatDto.setSenderId(memberId);
            chatDto.setMessage(sendMessageRequestDto.getMessage());
            if (chatRoomService.exists(roomId)) {
                // 메시지 저장
                chatService.saveChatMessage(chatDto);

                SendMessageResponsetDto sendMessageResponsetDto = new SendMessageResponsetDto();
                sendMessageResponsetDto.setUniqueId(sendMessageRequestDto.getUniqueId());
                sendMessageResponsetDto.setSenderId(memberId);
                sendMessageResponsetDto.setRoomId(roomId);
                sendMessageResponsetDto.setSenderEmail(jwtEmail);
                sendMessageResponsetDto.setMessage(chatDto.getMessage());
                sendMessageResponsetDto.setSentTime(chatDto.getSentTime());
                return sendMessageResponsetDto; // 저장된 메시지 반환
            } else {
                throw new RuntimeException("채팅방이 존재하지 않습니다.");
            }
        } catch (Exception e) {
            log.error("메시지 전송 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("예상치 못한 에러가 발생했습니다.");
        }
    }

}
