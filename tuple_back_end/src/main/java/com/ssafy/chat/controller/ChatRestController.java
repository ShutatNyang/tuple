package com.ssafy.chat.controller;

import com.ssafy.chat.model.ChatDto;
import com.ssafy.chat.model.ChatRoomDto;
import com.ssafy.chat.model.dto.response.MyChatRoomDto;
import com.ssafy.chat.model.dto.response.PreviousChatResponseDto;
import com.ssafy.chat.model.service.ChatRoomService;
import com.ssafy.chat.model.service.ChatService;
import com.ssafy.util.JwtUtil;
import io.jsonwebtoken.Jwt;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/chat")
@Tag(name = "ChatController", description = "채팅방 개설, 1대1 채팅 기능 처리")
public class ChatRestController {

    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    @Autowired
    public ChatRestController(ChatRoomService chatRoomService, ChatService chatService) {
        this.chatRoomService = chatRoomService;
        this.chatService = chatService;
    }

    @PostMapping("/room")
    @Operation(summary = "채팅방 찾기 및 생성", description = "채팅방을 생성하거나 참가중인 채팅방을 가져옵니다.")
    public ResponseEntity<?> findOrCreateChatRoom(@RequestParam("participant1Id") int participant1Id, @RequestParam("participant2Id") int participant2Id) {
        try {
            ChatRoomDto chatRoomDto = chatRoomService.findOrCreateChatRoom(participant1Id, participant2Id);
            return ResponseEntity.ok(chatRoomDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/previous-messages")
    @Operation(summary = "메세지 가져오기", description = "채팅방의 이전 채팅 기록을 pageNavigation 기법을 사용하여 가져옵니다.")
    public ResponseEntity<?> getRecentChats(@RequestParam("roomId") int roomId,
                                            @RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestHeader("Authorization") String token) {

        token = token.replace("Bearer ", "");

        String jwtEmail = JwtUtil.getEmailFromToken(token);

        try {
            List<PreviousChatResponseDto> chats = chatService.getRecentChats(roomId, page, size, jwtEmail);
            return ResponseEntity.ok(chats);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/my-chat")
    public ResponseEntity<?> getMyChatRooms(@RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");

        if (!JwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String jwtEmail = JwtUtil.getEmailFromToken(token);

        try {
            int myMemberId = chatRoomService.getMemberIdByEmail(jwtEmail);

            List<MyChatRoomDto> myChatRooms = chatRoomService.getMyChatRoom(myMemberId);
            return ResponseEntity.ok(myChatRooms);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
