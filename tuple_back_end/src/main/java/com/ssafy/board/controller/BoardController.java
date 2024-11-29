package com.ssafy.board.controller;

import com.ssafy.board.model.BoardDto;
import com.ssafy.board.model.dto.request.BoardUpdateRequestDto;
import com.ssafy.board.model.dto.request.RecommendStatusRequestDto;
import com.ssafy.board.model.dto.response.BoardItemDto;
import com.ssafy.board.model.service.BoardService;
import com.ssafy.file.model.FileDto;
import com.ssafy.file.model.service.FileService;
import com.ssafy.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Slf4j
@RestController
@RequestMapping("/board")
@Tag(name = "BoardController", description = "게시판 관리 기능 제공")
public class BoardController {
    private final BoardService boardService;
    private final FileService fileService;

    @Autowired
    public BoardController(BoardService boardService, FileService fileService) {
        this.boardService = boardService;
        this.fileService = fileService;
    }

    @Operation(summary = "게시글 등록", description = "새로운 게시글을 등록합니다.")
    @PostMapping("/insert")
    public ResponseEntity<?> insert(@RequestBody BoardDto boardDto, @RequestHeader("Authorization") String token) {
        try {
            // 토큰에서 사용자 이메일 추출
            token = token.replace("Bearer ", "");
            String jwtEmail = JwtUtil.getEmailFromToken(token);
            boardDto.setMemberId(boardService.getMemberIdByEmail(jwtEmail));

            // HTML Sanitization: content 필드 정리
            String sanitizedContent = Jsoup.clean(boardDto.getContent(), Safelist.basicWithImages());
            boardDto.setContent(sanitizedContent);

            // 게시글 저장
            int cnt = boardService.insert(boardDto);
            if (cnt > 0) {
                return ResponseEntity.ok(boardDto.getId());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            log.error("게시글 등록 중 오류:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Operation(summary = "게시글 삭제", description = "게시글 ID를 사용하여 게시글을 삭제합니다.")
    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam("id") int id, @RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");

            if (!JwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
            }

            String jwtEmail = JwtUtil.getEmailFromToken(token);

            String boardEmail = boardService.getEmailByBoardId(id);
            log.debug("boardEmail: " + boardEmail);

            if (!jwtEmail.equals(boardEmail)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
            }

            int cnt = boardService.delete(id);
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


    @Operation(summary = "게시글 수정", description = "기존 게시글의 내용을 수정합니다.")
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody BoardUpdateRequestDto boardUpdateRequestDto, @RequestHeader("Authorization") String token) {
        try {
            // 1. 토큰 검증 및 이메일 추출
            token = token.replace("Bearer ", "");
            if (!JwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
            }

            String jwtEmail = JwtUtil.getEmailFromToken(token);

            log.debug("boardUpdateRequestDto: " + boardUpdateRequestDto);
            // 2. 게시글 작성자 검증
            String boardEmail = boardService.getEmailByBoardMemberId(boardUpdateRequestDto.getMemberId());
            if (!jwtEmail.equals(boardEmail)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
            }

            // 3. HTML Sanitization (Jsoup 사용)
            String sanitizedContent = Jsoup.clean(boardUpdateRequestDto.getContent(), Safelist.basicWithImages());
            boardUpdateRequestDto.setContent(sanitizedContent);

            // 4. 게시글 수정 처리
            int cnt = boardService.update(boardUpdateRequestDto);
            if (cnt > 0) {
                // 이미지 연관 업데이트 처리
                fileService.updateAssociatedId("board", boardUpdateRequestDto.getId());
                return ResponseEntity.ok(boardUpdateRequestDto.getId());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("게시글 수정 실패");
            }
        } catch (Exception e) {
            log.error("게시글 수정 중 오류 발생:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }

    @Operation(summary = "전체 게시글 목록 조회", description = "모든 게시글 목록을 조회합니다.")
    @PostMapping("/list/all")
    public ResponseEntity<?> lengthOfListAll(@RequestParam("boardType") String boardType) {
        try {
            int length = boardService.lengthOfListAll(boardType);
            return ResponseEntity.ok(length);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "카테고리별 게시글 조회", description = "특정 카테고리에 속하는 게시글 목록을 조회합니다.")
    @PostMapping("/list/category")
    public ResponseEntity<?> listCategory(@RequestParam(value = "boardType") String boardType, @RequestParam("page") int page, @RequestParam("size") int size) {
        try {
            log.debug("boardType: " + boardType + " page: " + page + " size: " + size);
            List<BoardItemDto> list = boardService.listByCategory(boardType, page, size);
            if (Objects.isNull(list)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            } else {
                return ResponseEntity.ok(list);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "게시글 상세 조회", description = "게시글 ID를 사용하여 게시글의 상세 정보를 조회합니다.")
    @PostMapping("/detail")
    public ResponseEntity<?> detail(@RequestParam(value = "id") int id) {
        try {
            BoardItemDto boardItemDto = boardService.detail(id);
            if (Objects.isNull(boardItemDto)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            log.debug("Board Detail Response Dto : " + boardItemDto);
            return ResponseEntity.ok(boardItemDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "게시글 추천/비추천", description = "게시글 ID와 좋아요 여부를 사용하여 게시글의 추천 또는 비추천 수를 증가시킵니다.")
    @PostMapping("/recommend")
    public ResponseEntity<?> recommend(@RequestParam("boardId") int boardId, @RequestParam("like") boolean isLike, @RequestHeader("Authorization") String token) {

        try {
            token = token.replace("Bearer ", "");
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("memberId", boardService.getMemberIdByEmail(JwtUtil.getEmailFromToken(token)));
            paramMap.put("boardId", boardId);
            paramMap.put("isLike", isLike);

            // 좋아요/비추천 추가 시도
            int insertResult = boardService.insertLikeDislike(paramMap);

            // 이미 추천/비추천 한 경우 처리
            if (insertResult == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 처리된 항목입니다.");
            }

            // 좋아요 또는 비추천 수 증가 처리
            int updateCount;
            if (isLike) {
                updateCount = boardService.incrementRecommendCount(boardId);
            } else {
                updateCount = boardService.incrementNotRecommendCount(boardId);
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
    public ResponseEntity<?> cancelRecommend(@RequestParam("boardId") int boardId, @RequestParam("like") int like, @RequestHeader("Authorization") String token) {
        try {
            // 토큰 인증
            token = token.replace("Bearer ", "");
            String jwtEmail = JwtUtil.getEmailFromToken(token);
            int memberId = boardService.getMemberIdByEmail(jwtEmail);
            log.debug("memberId : " + memberId);

            // 파라미터 설정
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("memberId", memberId);
            paramMap.put("boardId", boardId);

            log.debug(paramMap.toString());

            // 추천/비추천 취소 시도
            int cancelResult = boardService.cancelLikeDislike(paramMap);

            // 취소 결과 처리
            if (cancelResult == 1) {
                int decrementResult = 0;
                if (like == 1) {
                    decrementResult = boardService.decrementRecommendCount(boardId);
                } else {
                    decrementResult = boardService.decrementNotRecommendCount(boardId);
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
    public ResponseEntity<?> getRecommenStatus(RecommendStatusRequestDto recommendStatusRequestDto, @RequestHeader("Authorization") String token) {

        token = token.replace("Bearer ", "");

        if (!JwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }

        String jwtEmail = JwtUtil.getEmailFromToken(token);

        try {
            int memberId = boardService.getMemberIdByEmail(jwtEmail);
            recommendStatusRequestDto.setMemberId(memberId);

            log.debug(recommendStatusRequestDto.toString());

            int isLike = boardService.getIsLike(recommendStatusRequestDto);
            log.debug("isLike : " + isLike);

            return ResponseEntity.ok(isLike);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

