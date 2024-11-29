package com.ssafy.comment.model.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentRecommendStatusRequestDto {
    private int commentId;
    private int memberId;
}
