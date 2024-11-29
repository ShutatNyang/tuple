package com.ssafy.board.model.service;

import com.ssafy.board.model.BoardDto;
import com.ssafy.board.model.dto.request.BoardUpdateRequestDto;
import com.ssafy.board.model.dto.request.RecommendStatusRequestDto;
import com.ssafy.board.model.dto.response.BoardItemDto;
import com.ssafy.board.model.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BoardServiceImpl implements BoardService {

    private BoardMapper boardMapper;

    @Autowired
    public BoardServiceImpl(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    @Override
    public int insert(BoardDto boardDto) throws Exception {
        if (boardDto.getTitle() == null || boardDto.getTitle().trim().isEmpty() || boardDto.getContent() == null || boardDto.getContent().isEmpty()) {
            return 0;
        }
        return boardMapper.insert(boardDto);
    }

    @Override
    public int delete(int id) throws Exception {
        return boardMapper.delete(id);
    }

    @Override
    public int update(BoardUpdateRequestDto boardUpdateRequestDto) throws Exception {
        if (boardUpdateRequestDto.getTitle() == null || boardUpdateRequestDto.getTitle().trim().isEmpty() || boardUpdateRequestDto.getContent() == null || boardUpdateRequestDto.getContent().isEmpty()) {
            return 0;
        }

        BoardDto boardDto = new BoardDto();
        boardDto.setTitle(boardUpdateRequestDto.getTitle());
        boardDto.setContent(boardUpdateRequestDto.getContent());
        boardDto.setId(boardUpdateRequestDto.getId());
        return boardMapper.update(boardDto);
    }

    @Override
    public int lengthOfListAll(String boardType) throws Exception {
        return boardMapper.lengthOfListAll(boardType);
    }

    @Override
    public List<BoardItemDto> listByCategory(String boardType, int page, int size) throws Exception {
        int offset = (page - 1) * size;
        return boardMapper.listByCategory(boardType, offset, size);
    }

    @Override
    public BoardItemDto detail(int id) throws Exception {
        BoardDto boardDto = boardMapper.detail(id);

        BoardItemDto boardItemDto = new BoardItemDto();
        boardItemDto.setId(boardDto.getId());
        boardItemDto.setTitle(boardDto.getTitle());
        boardItemDto.setContent(boardDto.getContent());
        boardItemDto.setBoardType(boardDto.getBoardType());
        boardItemDto.setRegistTime(boardDto.getRegistTime());
        boardItemDto.setLikeCount(boardDto.getLikeCount());
        boardItemDto.setDislikeCount(boardDto.getDislikeCount());
        boardItemDto.setMemberId(boardDto.getMemberId());
        boardItemDto.setEmail(boardMapper.getEmailByBoardMemberId(boardDto.getMemberId()));
        return boardItemDto;
    }

    @Override
    public int incrementRecommendCount(int id) throws Exception {
        return boardMapper.incrementRecommendCount(id);
    }

    @Override
    public int insertLikeDislike(Map<String, Object> paramMap) throws Exception {
        return boardMapper.insertLikeDislike(paramMap);
    }

    @Override
    public int cancelLikeDislike(Map<String, Object> paramMap) throws Exception {
        return boardMapper.cancelLikeDislike(paramMap);
    }

    @Override
    public int decrementRecommendCount(int id) throws Exception {
        return boardMapper.decrementRecommendCount(id);
    }

    @Override
    public int decrementNotRecommendCount(int id) throws Exception {
        return boardMapper.decrementNotRecommendCount(id);
    }

    @Override
    public int incrementNotRecommendCount(int id) throws Exception {
        return boardMapper.incrementNotRecommendCount(id);
    }

    @Override
    public String getEmailByBoardMemberId(int memberId) throws Exception {
        return boardMapper.getEmailByBoardMemberId(memberId);
    }

    @Override
    public String getEmailByBoardId(int id) throws Exception {
        return boardMapper.getEmailByBoardId(id);
    }

    @Override
    public int getMemberIdByEmail(String email) throws Exception {
        return boardMapper.getMemberIdByEmail(email);
    }

    @Override
    public int getIsLike(RecommendStatusRequestDto recommendStatusRequestDto) throws Exception {
        return boardMapper.getIsLike(recommendStatusRequestDto);
    }
}
