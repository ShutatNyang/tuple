package com.ssafy.comment.model.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentListResponseDto {
    private int id;
    private int memberId;
    private int boardId;
    private int likeCount;
    private int dislikeCount;
    private String createdTime;
    private String content;
    private String deleteTime;
    private String updatedTime;
    private String email;
}
