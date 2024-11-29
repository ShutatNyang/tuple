package com.ssafy.comment.model.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentDeleteRequestDto {
    private int id;
    private int boardId;
    private int memberId;
    private String email;
}
