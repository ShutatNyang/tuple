package com.ssafy.chat.model.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SendMessageRequestDto {
    private String uniqueId;
    private String message;
}
