package com.ssafy.chat.model.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyChatRoomDto {
    int id;
    String myEmail;
    String partnerEmail;
    String partnerId;
}
