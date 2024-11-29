package com.ssafy.chat.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChatRoomDto {
    private int id;
    private int participant1Id;
    private int participant2Id;
    private String createdTime;
}
