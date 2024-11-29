package com.ssafy.comment.model.mapper;

import com.ssafy.comment.model.CommentDto;
import com.ssafy.comment.model.dto.request.CommentDeleteRequestDto;
import com.ssafy.comment.model.dto.request.CommentRecommendStatusRequestDto;
import com.ssafy.comment.model.dto.response.CommentListResponseDto;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Mapper
public interface CommentMapper {

    int insert(CommentDto commentDto)throws Exception;

    int delete(CommentDeleteRequestDto commentDeleteRequestDto)throws Exception;

    List<CommentListResponseDto> listAll(int boardId, int limit) throws Exception;

    int incrementRecommendCount(int id) throws Exception;

    int incrementNotRecommendCount(int id) throws Exception;

    int insertLikeDislike(Map<String, Object> paramMap)throws Exception;

    int cancelLikeDislike(Map<String, Object> paramMap)throws Exception;

    int decrementRecommendCount(int id) throws Exception;

    int decrementNotRecommendCount(int id) throws Exception;

    String getEmailByMemberId(int memberId) throws Exception;

    String getEmailByCommentId(int id) throws Exception;

    int getMemberIdByEmail(String email) throws Exception;

    int getIsLike(CommentRecommendStatusRequestDto commentRecommendStatusRequestDto) throws SQLException;

    int countAllCommentByBoardId(int boardId) throws SQLException;
}
