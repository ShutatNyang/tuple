package com.ssafy.tourplan.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TourPlanDto {
    private int id; // 고유 ID
    private int ownerId; // 작성자 ID
    private String planTitle;
    private String startDate; // 여행 시작 날짜
    private String endDate; // 여행 종료 날짜
    private String startLocation; // 출발 위치
    private String vehicle; // 사용 차량
    private String toTourVehicle; // 관광 시 사용 차량
    private String plan; // 여행 계획 (JSON 형식)
    private String createDate; // 생성 날짜
    private String planDescription;
}
