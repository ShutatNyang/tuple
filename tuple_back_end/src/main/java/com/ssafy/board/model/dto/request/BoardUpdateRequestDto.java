package com.ssafy.board.model.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardUpdateRequestDto {
    private int id;
    private String title;
    private String content;
    private int memberId;
}
