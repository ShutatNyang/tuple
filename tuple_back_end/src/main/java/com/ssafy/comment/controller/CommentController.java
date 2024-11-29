package com.ssafy.comment.controller;


import com.ssafy.comment.model.CommentDto;
import com.ssafy.comment.model.dto.request.CommentDeleteRequestDto;
import com.ssafy.comment.model.dto.request.CommentRecommendStatusRequestDto;
import com.ssafy.comment.model.dto.response.CommentListResponseDto;
import com.ssafy.comment.model.service.CommentService;
import com.ssafy.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/comment")
@Tag(name = "CommentController", description = "댓글 관리 기능 제공")
public class CommentController {
    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @Operation(summary = "댓글 등록", description = "새로운 댓글을 등록합니다.")
    @PostMapping("/insert")
    public ResponseEntity<?> insert(CommentDto commentDto, @RequestHeader("Authorization") String token){
        token = token.replace("Bearer ", "");

        if (!JwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String jwtEmail = JwtUtil.getEmailFromToken(token);
        try {
            commentDto.setMemberId(commentService.getMemberIdByEmail(jwtEmail));
            log.debug("commentDto: " + commentDto);
            int cnt = commentService.insert(commentDto);
            if (cnt == 1) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "댓글 삭제", description = "댓글 ID를 사용하여 댓글을 삭제합니다.")
    @PostMapping("/delete")
    public ResponseEntity<?> delete(CommentDeleteRequestDto commentDeleteRequestDto,
                                    @RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");

            if (!JwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
            }

            String jwtEmail = JwtUtil.getEmailFromToken(token); //로그인 된 이메일
            String commentEamil = commentDeleteRequestDto.getEmail();

            if (!jwtEmail.equals(commentEamil)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
            }

            commentDeleteRequestDto.setMemberId(commentService.getMemberIdByEmail(jwtEmail));
            log.debug(commentDeleteRequestDto.toString());

            int cnt = commentService.delete(commentDeleteRequestDto);
            if (cnt == 1) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "글별 댓글 조회", description = "특정 글에 속하는 댓글 목록을 조회합니다.")
    @PostMapping("/{boardId}")
    public ResponseEntity<?> listCommentByBoardId(@PathVariable(value = "boardId") int boardId, @RequestParam("page") int page, @RequestParam("size") int size) {

        try {
            List<CommentListResponseDto> list = commentService.listAll(boardId, page, size);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "댓글 추천/비추천", description = "댓글 ID와 좋아요 여부를 사용하여 댓글의 추천 또는 비추천 수를 증가시킵니다.")
    @PostMapping("/recommend")
    public ResponseEntity<?> recommend(@RequestParam("commentId") int commentId, @RequestParam("like") boolean isLike, @RequestHeader("Authorization") String token){

        try {
            token = token.replace("Bearer ", "");
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("memberId", commentService.getMemberIdByEmail(JwtUtil.getEmailFromToken(token)));
            paramMap.put("commentId", commentId);
            paramMap.put("isLike", isLike);

            log.debug(paramMap.toString());

            // 좋아요/비추천 추가 시도
            int insertResult = commentService.insertLikeDislike(paramMap);

            // 이미 추천/비추천 한 경우 처리
            if (insertResult == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 처리된 항목입니다.");
            }

            // 좋아요 또는 비추천 수 증가 처리
            int updateCount;
            if (isLike) {
                updateCount = commentService.incrementRecommendCount(commentId);
            } else {
                updateCount = commentService.incrementNotRecommendCount(commentId);
            }

            // 업데이트 성공 여부 확인
            if (updateCount == 1) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("추천/비추천 수 업데이트 실패");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "추천/비추천 취소", description = "추천 또는 비추천을 취소합니다.")
    @PostMapping("/recommend/cancel")
    public ResponseEntity<?> cancelRecommend(
            @RequestParam("commentId") int commentId, //댓글 pk 번호
            @RequestParam("like") int like,
            @RequestHeader("Authorization") String token) {
        try {
            // 토큰 인증
            token = token.replace("Bearer ", "");
            String jwtEmail = JwtUtil.getEmailFromToken(token);
            int memberId = commentService.getMemberIdByEmail(jwtEmail);

            // 파라미터 설정
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("memberId", memberId);
            paramMap.put("commentId", commentId);

            // 추천/비추천 취소 시도
            int cancelResult = commentService.cancelLikeDislike(paramMap);

            // 취소 결과 처리
            if (cancelResult == 1) {
                int decrementResult = 0;
                if (like == 1) {
                    decrementResult = commentService.decrementRecommendCount(commentId);
                } else {
                    decrementResult = commentService.decrementNotRecommendCount(commentId);
                }

                if (decrementResult == 0) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("취소할 항목이 없습니다.");
                }
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("취소할 항목이 없습니다.");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/recommend/status")
    @Operation(summary = "추천/비추천 조회", description = "추천 또는 비추천을 조회합니다.")
    public ResponseEntity<?> getRecommendStatus(CommentRecommendStatusRequestDto commentRecommendStatusRequestDto, @RequestHeader("Authorization") String token) {

        token = token.replace("Bearer ", "");

        if (!JwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }

        String jwtEmail = JwtUtil.getEmailFromToken(token);

        try {
            int memberId = commentService.getMemberIdByEmail(jwtEmail);
            commentRecommendStatusRequestDto.setMemberId(memberId);

            log.debug(commentRecommendStatusRequestDto.toString());

            int isLike = commentService.getIsLike(commentRecommendStatusRequestDto);
            log.debug("isLike : " + isLike);

            return ResponseEntity.ok(isLike);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/count")
    public ResponseEntity<?> countAllCommentByBoradId(@RequestParam("boardId") int boardId) {
        try {
            int cnt = commentService.countAllCommentByBoardId(boardId);
            return ResponseEntity.ok(cnt);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
