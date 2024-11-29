package com.ssafy.carrentalcompany.controller;

import com.ssafy.carrentalcompany.model.CarRentalCompanyDto;
import com.ssafy.carrentalcompany.model.dto.RequestByCodesDto;
import com.ssafy.carrentalcompany.model.service.CarRentalCompanyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/car-rental")
@Tag(name = "CarRentalCompanyController", description = "내 지역 주변 렌터카 추천, 여행지 주변 렌터카 추천 기능 처리")
public class CarRentalCompanyController {

    private final CarRentalCompanyService carRentalCompanyService;

    @Autowired
    public CarRentalCompanyController(CarRentalCompanyService carRentalCompanyService) {
        this.carRentalCompanyService = carRentalCompanyService;
    }

    @PostMapping("/list/all")
    @Operation(summary = "전체 조회", description = "렌터카 회사 데이터베이스 전체 조회")
    public ResponseEntity<?> listAll() {
        try {
            List<CarRentalCompanyDto> list = carRentalCompanyService.listAll();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/list/address")
    @Operation(summary = "지번 주소로 렌터카 회사 조회", description = "지번 주소를 사용하여 해당 지역의 렌터카 회사 조회")
    public ResponseEntity<?> listByAddress(@RequestBody String addressJibun) {
        try {
            List<CarRentalCompanyDto> list = carRentalCompanyService.listByAddress(addressJibun);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/list/getByCodes")
    public ResponseEntity<?> getByCodes(@RequestBody RequestByCodesDto requestByCodesDto){

        try{
            List<CarRentalCompanyDto> list = carRentalCompanyService.listByCodes(requestByCodesDto);
            return ResponseEntity.ok(list);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
