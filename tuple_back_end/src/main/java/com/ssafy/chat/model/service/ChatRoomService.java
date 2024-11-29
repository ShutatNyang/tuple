package com.ssafy.chat.model.service;

import com.ssafy.chat.model.ChatRoomDto;
import com.ssafy.chat.model.dto.response.MyChatRoomDto;

import java.sql.SQLException;
import java.util.List;

public interface ChatRoomService {

    ChatRoomDto findOrCreateChatRoom(int participant1Id, int participant2Id) throws Exception;

    boolean exists(int roomId) throws Exception;

    int getMemberIdByEmail(String email) throws Exception;

    List<MyChatRoomDto> getMyChatRoom(int memberId) throws Exception;
}
