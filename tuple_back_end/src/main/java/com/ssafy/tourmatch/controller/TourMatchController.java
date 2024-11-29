package com.ssafy.tourmatch.controller;

import com.ssafy.chat.model.service.ChatRoomService;
import com.ssafy.tourmatch.model.TourMatchDto;
import com.ssafy.tourmatch.model.TourMatchRequestDto;
import com.ssafy.tourmatch.model.dto.request.AcceptRejectMatchRequestDto;
import com.ssafy.tourmatch.model.dto.request.RequestMatchDto;
import com.ssafy.tourmatch.model.service.TourMatchRequestService;
import com.ssafy.tourmatch.model.service.TourMatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tour-match")
@Tag(name = "TourMatchController", description = "여행 계획 매칭 기능 처리")
public class TourMatchController {

    private final TourMatchService tourMatchService;
    private final TourMatchRequestService tourMatchRequestService;
    private final ChatRoomService chatRoomService;

    @Autowired
    public TourMatchController(TourMatchService tourMatchService, TourMatchRequestService tourMatchRequestService, ChatRoomService chatRoomService) {
        this.tourMatchService = tourMatchService;
        this.tourMatchRequestService = tourMatchRequestService;
        this.chatRoomService = chatRoomService;
    }

    @PostMapping("/insert")
    public ResponseEntity<?> insert(@RequestBody TourMatchDto tourMatchDto) {
        try {
            int cnt = tourMatchService.insert(tourMatchDto);

            if (cnt == 1) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (SQLIntegrityConstraintViolationException e) { // JDBC에서 발생하는 예외
            log.error("Unique constraint violation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // HTTP 409 CONFLICT 반환
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body("An unexpected error occurred.");
        }
    }

    @PostMapping("/request")
    @Operation(summary = "매칭 요청", description = "여행 계획을 보고 같이 가고 싶은 여행에 대해 매칭을 요청합니다.")
    public ResponseEntity<?> requestMatch(@RequestBody RequestMatchDto requestMatchDto) {

        try {
            int cnt = tourMatchRequestService.insert(requestMatchDto);
            return cnt == 1 ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("Failed to request match.");
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body("An unexpected error occurred.");
        }
    }

    @PostMapping("/list/my-match")
    public ResponseEntity<?> getMyMatchtList(@RequestParam("memberId") int memberId) {
        try {
            List<TourMatchDto> list = tourMatchService.getMyMatchList(memberId);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body("An unexpected error occurred.");
        }
    }

    @PostMapping("/list/my-match/{tourPlanId}")
    @Operation(summary = "내 계획에 매칭 신청을 한 사람들 목록", description = "내 여행 매칭 리스트를 불러와서 매칭 신청자를 확인합니다.")
    public ResponseEntity<?> getMatchRequests(@RequestParam(value = "memberId") int memberId, @PathVariable int tourPlanId) {
        try {
            List<TourMatchRequestDto> list = tourMatchRequestService.getMyMatchRequestPeople(memberId, tourPlanId);
            return ResponseEntity.ok(list);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body("An unexpected error occurred.");
        }
    }

    @PostMapping("/list/await")
    public ResponseEntity<?> getAllMatch(@RequestParam("memberId") int memberId) {
        try {
            List<TourMatchDto> list = tourMatchService.getAllMatch(memberId);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body("An unexpected error occurred.");
        }
    }

    @PostMapping("/list/request")
    public ResponseEntity<?> getMyMatchRequestList(@RequestParam("partnerId") int partnerId) {
        try {
            List<TourMatchRequestDto> list = tourMatchRequestService.getMyMatchRequestList(partnerId);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body("An unexpected error occurred.");
        }
    }

    @PostMapping("/respond")
    @Operation(summary = "매칭 응답", description = "매칭 요청에 대한 응답을 설정합니다.")
    public ResponseEntity<?> respondToMatch(AcceptRejectMatchRequestDto acceptRejectMatchRequestDto) {
        try {
            if ("matched".equals(acceptRejectMatchRequestDto.getStatus())) {
                tourMatchService.setMatched(acceptRejectMatchRequestDto.getTourPlanId());
                tourMatchRequestService.setRejectRestRequest(acceptRejectMatchRequestDto);
                chatRoomService.findOrCreateChatRoom(acceptRejectMatchRequestDto.getMemberId(), acceptRejectMatchRequestDto.getPartnerId());
                int cnt = tourMatchRequestService.setStatus(acceptRejectMatchRequestDto);
                return cnt == 1 ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("Failed to respond to match.");
            } else {
                int cnt = tourMatchRequestService.setStatus(acceptRejectMatchRequestDto);
                return cnt == 1 ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("Failed to respond to match.");
            }
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body("An unexpected error occurred.");
        }
    }
}
