package com.ssafy.board.model.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardItemDto {
    private int id;
    private int memberId;
    private String email;
    private String boardType;
    private String registTime;
    private String likeCount;
    private String dislikeCount;
    private String title;
    private String content;
    private String deleteTime;
    private String updatedTime;
}
