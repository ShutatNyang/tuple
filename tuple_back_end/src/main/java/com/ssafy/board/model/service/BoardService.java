package com.ssafy.board.model.service;

import com.ssafy.board.model.BoardDto;
import com.ssafy.board.model.dto.request.BoardUpdateRequestDto;
import com.ssafy.board.model.dto.request.RecommendStatusRequestDto;
import com.ssafy.board.model.dto.response.BoardItemDto;

import java.util.List;
import java.util.Map;

public interface BoardService {

    // 글 등록
    int insert(BoardDto boardDto) throws Exception;

    // 글 삭제
    int delete(int id) throws Exception;

    // 글 수정
    int update(BoardUpdateRequestDto boardUpdateRequestDto) throws Exception;

    // 글 전체 조회
    int lengthOfListAll(String boardType) throws Exception;

    // 카테고리별 글 목록 조회
    List<BoardItemDto> listByCategory(String boardType, int page, int size) throws Exception;

    // 글 상세 조회
    BoardItemDto detail(int id) throws Exception;

    //글 좋아요+1
    int incrementRecommendCount(int id) throws Exception;

    //글 비추+1
    int incrementNotRecommendCount(int id) throws Exception;

    //중복 확인용 테이블 추가
    int insertLikeDislike(Map<String, Object> paramMap) throws Exception;

    //중복 확인용 테이블 삭제
    int cancelLikeDislike(Map<String, Object> paramMap) throws Exception;

    //글 좋아요 - 1
    int decrementRecommendCount(int id) throws Exception;

    //글 싫어요 - 1
    int decrementNotRecommendCount(int id) throws Exception;

    //board의 meberId로 member의 email 가져오기
    String getEmailByBoardMemberId(int memberId) throws Exception;

    //board 작성자 email 가져오기
    String getEmailByBoardId(int id) throws Exception;

    int getMemberIdByEmail(String email) throws Exception;

    int getIsLike(RecommendStatusRequestDto recommendStatusRequestDto) throws Exception;

}
