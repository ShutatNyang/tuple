package com.ssafy.tourplan.controller;

import com.ssafy.tourplan.model.TourPlanDto;
import com.ssafy.tourplan.model.service.TourPlanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/tour-plan")
@Tag(name="TourPlanController", description = "여행 계획 관리 기능 제공")
public class TourPlanController {

    private final TourPlanService tourPlanService;

    @Autowired
    public TourPlanController(TourPlanService tourPlanService) {
        this.tourPlanService = tourPlanService;
    }

    @Operation(summary = "여행 계획 등록", description = "여행 계획을 등록합니다.")
    @PostMapping("/insert")
    public ResponseEntity<?> insert(@RequestBody TourPlanDto tourPlanDto) {
        try {
            int cnt = tourPlanService.insert(tourPlanDto);
            if (cnt == 1) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("여행 계획 등록 실패");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }

    @Operation(summary = "여행 계획 삭제", description = "여행 계획을 삭제합니다.")
    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam("id") int id, @RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");
            tourPlanService.validateMemberAuthorization(token, id);

            int cnt = tourPlanService.delete(id);
            if (cnt == 1) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("여행 계획 삭제 실패");
            }
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }

    @Operation(summary = "여행 계획 수정", description = "기존 여행 계획을 수정합니다.")
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody TourPlanDto tourPlanDto, @RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");
            tourPlanService.validateMemberAuthorization(token, tourPlanDto.getId());

            int cnt = tourPlanService.update(tourPlanDto);
            if (cnt == 1) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("여행 계획 수정 실패");
            }
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }

    @Operation(summary = "여행 계획 상세 조회", description = "여행 계획 ID를 사용하여 여행 계획의 상세 정보를 조회합니다.")
    @PostMapping("/detail")
    public ResponseEntity<?> detail(@RequestParam(value = "id") int id) {
        try {
            TourPlanDto tourPlanDto = tourPlanService.detail(id);
            if (Objects.isNull(tourPlanDto)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("여행 계획이 존재하지 않습니다.");
            } else {
                return ResponseEntity.ok(tourPlanDto);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }

    @Operation(summary = "전체 여행 계획 목록 조회", description = "모든 여행 계획 목록을 조회합니다.")
    @PostMapping("/list/all")
    public ResponseEntity<?> listALL() {
        try {
            List<TourPlanDto> list = tourPlanService.listAll();
            if (list.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("조회된 여행 계획이 없습니다.");
            } else {
                return ResponseEntity.ok(list);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }

    @Operation(summary = "특정 여행지의 전체 여행 계획 목록 조회", description = "특정 여행지의 여행 계획 목록을 조회합니다.")
    @PostMapping("/list/start-position")
    public ResponseEntity<?> listByStartPosition(@RequestParam(value = "startPosition") String startPosition) {
        try {
            List<TourPlanDto> list = tourPlanService.listByStartPosition(startPosition);
            if (list.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("해당 위치의 여행 계획이 없습니다.");
            } else {
                return ResponseEntity.ok(list);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }

}
