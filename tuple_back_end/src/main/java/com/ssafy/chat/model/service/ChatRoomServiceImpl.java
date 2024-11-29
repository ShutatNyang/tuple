package com.ssafy.chat.model.service;

import com.ssafy.chat.model.ChatRoomDto;
import com.ssafy.chat.model.dto.response.MyChatRoomDto;
import com.ssafy.chat.model.mapper.ChatRoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomMapper chatRoomMapper;

    @Autowired
    public ChatRoomServiceImpl(ChatRoomMapper chatRoomMapper) {
        this.chatRoomMapper = chatRoomMapper;
    }

    @Override
    public ChatRoomDto findOrCreateChatRoom(int participant1Id, int participant2Id) throws Exception {
        ChatRoomDto chatRoomDto = chatRoomMapper.findChatRoomByMemberId(participant1Id, participant2Id);
        if (Objects.isNull(chatRoomDto)) {
            chatRoomDto = new ChatRoomDto();
            chatRoomDto.setParticipant1Id(participant1Id);
            chatRoomDto.setParticipant2Id(participant2Id);
            chatRoomMapper.createChatRoom(chatRoomDto);
        }
        return chatRoomDto;
    }

    @Override
    public boolean exists(int roomId) throws Exception {
        return chatRoomMapper.exists(roomId) > 0;
    }

    @Override
    public int getMemberIdByEmail(String email) throws Exception {
        return chatRoomMapper.getMemberIdByEmail(email);
    }

    @Override
    public List<MyChatRoomDto> getMyChatRoom(int memberId) throws Exception {
        return chatRoomMapper.getMyChatRoom(memberId);
    }
}
