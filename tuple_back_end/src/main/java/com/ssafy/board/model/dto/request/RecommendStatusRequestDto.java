package com.ssafy.board.model.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RecommendStatusRequestDto {
    private int boardId;
    private int memberId;
    private String targetType;
}
