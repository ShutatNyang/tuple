package com.ssafy.comment.model.service;

import com.ssafy.comment.model.CommentDto;
import com.ssafy.comment.model.dto.request.CommentDeleteRequestDto;
import com.ssafy.comment.model.dto.request.CommentRecommendStatusRequestDto;
import com.ssafy.comment.model.dto.response.CommentListResponseDto;
import com.ssafy.comment.model.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentMapper commentMapper;

    @Autowired
    public CommentServiceImpl(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Override
    public int insert(CommentDto commentDto) throws Exception {
        return commentMapper.insert(commentDto);
    }

    @Override
    public int delete(CommentDeleteRequestDto commentDeleteRequestDto) throws Exception {
        return commentMapper.delete(commentDeleteRequestDto);
    }

    @Override
    public List<CommentListResponseDto> listAll(int boardId, int page, int size) throws Exception {
        int limit = page * size;
        return commentMapper.listAll(boardId, limit);
    }

    @Override
    public int incrementRecommendCount(int id) throws Exception {
        return commentMapper.incrementRecommendCount(id);
    }

    @Override
    public int incrementNotRecommendCount(int id) throws Exception {
        return commentMapper.incrementNotRecommendCount(id);
    }

    @Override
    public int insertLikeDislike(Map<String, Object> paramMap) throws Exception {
        return commentMapper.insertLikeDislike(paramMap);
    }

    @Override
    public int cancelLikeDislike(Map<String, Object> paramMap) throws Exception {
        return commentMapper.cancelLikeDislike(paramMap);
    }

    @Override
    public int decrementRecommendCount(int id) throws Exception {
        return commentMapper.decrementRecommendCount(id);
    }

    @Override
    public int decrementNotRecommendCount(int id) throws Exception {
        return commentMapper.decrementNotRecommendCount(id);
    }

    @Override
    public String getEmailByMemberId(int memberId) throws Exception {
        return commentMapper.getEmailByMemberId(memberId);
    }

    @Override
    public String getEmailByCommentId(int id) throws Exception {
        return commentMapper.getEmailByCommentId(id);
    }

    @Override
    public int getMemberIdByEmail(String email) throws Exception {
        return commentMapper.getMemberIdByEmail(email);
    }

    @Override
    public int getIsLike(CommentRecommendStatusRequestDto commentRecommendStatusRequestDto) throws Exception {
        return commentMapper.getIsLike(commentRecommendStatusRequestDto);
    }

    @Override
    public int countAllCommentByBoardId(int boardId) throws Exception {
        return commentMapper.countAllCommentByBoardId(boardId);
    }


}
