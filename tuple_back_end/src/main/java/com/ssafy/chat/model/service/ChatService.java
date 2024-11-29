package com.ssafy.chat.model.service;

import com.ssafy.chat.model.ChatDto;
import com.ssafy.chat.model.dto.response.PreviousChatResponseDto;

import java.util.List;

public interface ChatService {
    List<PreviousChatResponseDto> getRecentChats(int roomId, int page, int size, String email) throws Exception;

    int saveChatMessage(ChatDto chatDto) throws Exception;
}
