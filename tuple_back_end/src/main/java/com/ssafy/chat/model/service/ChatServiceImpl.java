package com.ssafy.chat.model.service;

import com.ssafy.chat.model.ChatDto;
import com.ssafy.chat.model.dto.response.PreviousChatResponseDto;
import com.ssafy.chat.model.mapper.ChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatMapper chatMapper;

    @Autowired
    public ChatServiceImpl(ChatMapper chatMapper) {
        this.chatMapper = chatMapper;
    }

    @Override
    public List<PreviousChatResponseDto> getRecentChats(int roomId, int page, int size, String email) throws Exception {
        int offset = (page - 1) * size;
        return chatMapper.getRecentChats(roomId, size, offset, email);
    }

    @Override
    public int saveChatMessage(ChatDto chatDto) throws Exception{
        if (Objects.isNull(chatDto)) {
            throw new IllegalArgumentException("유효하지 않은 채팅입니다.");
        }
        return chatMapper.saveChatMessage(chatDto);
    }
}
