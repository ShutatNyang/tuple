package com.ssafy.comment.model.service;

import com.ssafy.comment.model.CommentDto;
import com.ssafy.comment.model.dto.request.CommentDeleteRequestDto;
import com.ssafy.comment.model.dto.request.CommentRecommendStatusRequestDto;
import com.ssafy.comment.model.dto.response.CommentListResponseDto;

import java.util.List;
import java.util.Map;


public interface CommentService {
    //댓글 등록
    int insert(CommentDto commentDto)throws Exception;

    int delete(CommentDeleteRequestDto commentDeleteRequestDto) throws Exception;

    //글 번호로 해당 글 댓글 전체 조회
    List<CommentListResponseDto> listAll(int boardId, int page, int size) throws Exception;

    //글 좋아요+1
    int incrementRecommendCount(int id) throws Exception;

    //글 비추+1
    int incrementNotRecommendCount(int id) throws Exception;

    //중복 확인용 테이블 추가
    int insertLikeDislike(Map<String, Object> paramMap)throws Exception;

    //중복 확인용 테이블 삭제
    int cancelLikeDislike(Map<String, Object> paramMap)throws Exception;

    //글 좋아요 - 1
    int decrementRecommendCount(int id) throws Exception;

    //글 싫어요 - 1
    int decrementNotRecommendCount(int id) throws Exception;

    //memberId로 Email 가져오기
    String getEmailByMemberId(int memberId) throws Exception;

    // boardId로 Email 가져오기
    String getEmailByCommentId(int id) throws Exception;

    // Email로 memberId 가져오기
    int getMemberIdByEmail(String email) throws Exception;

    int getIsLike(CommentRecommendStatusRequestDto commentRecommendStatusRequestDto) throws Exception;

    int countAllCommentByBoardId(int boardId) throws Exception;
}
