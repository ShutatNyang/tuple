package com.ssafy.chat.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChatDto {
    private int id;         // chat 테이블의 id 컬럼
    private int roomId;     // chat 테이블의 room_id 컬럼
    private int senderId;   // chat 테이블의 sender_id 컬럼
    private String message; // chat 테이블의 message 컬럼
    private String sentTime; // chat 테이블의 sent_time 컬럼
}
