package com.ssafy.chat.model.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessageResponsetDto {
    private String uniqueId;
    private int roomId;
    private String senderEmail;
    private int senderId;
    private String message;
    private String sentTime;
}
