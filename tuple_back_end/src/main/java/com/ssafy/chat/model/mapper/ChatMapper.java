package com.ssafy.chat.model.mapper;

import com.ssafy.chat.model.ChatDto;
import com.ssafy.chat.model.dto.response.PreviousChatResponseDto;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.List;

@Mapper
public interface ChatMapper {

    List<PreviousChatResponseDto> getRecentChats(int roomId, int limit, int offset, String email) throws SQLException;

    int saveChatMessage(ChatDto chatDto) throws SQLException;

}
