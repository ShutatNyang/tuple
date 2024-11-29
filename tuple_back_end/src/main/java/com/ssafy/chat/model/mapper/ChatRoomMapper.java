package com.ssafy.chat.model.mapper;

import com.ssafy.chat.model.ChatRoomDto;
import com.ssafy.chat.model.dto.response.MyChatRoomDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.jdbc.SQL;

import java.sql.SQLException;
import java.util.List;

@Mapper
public interface ChatRoomMapper {

    ChatRoomDto findChatRoomByMemberId(int participant1Id, int participant2Id) throws SQLException;

    void createChatRoom(ChatRoomDto chatRoomDto) throws SQLException;

    int exists(int roomId) throws SQLException;

    int getMemberIdByEmail(String email) throws SQLException;

    List<MyChatRoomDto> getMyChatRoom(int memberId) throws SQLException;
}
