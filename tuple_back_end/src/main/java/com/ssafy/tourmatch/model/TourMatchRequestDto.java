package com.ssafy.tourmatch.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TourMatchRequestDto {
    private int id;
    private int memberId;
    private int tourPlanId;
    private int partnerId;
    private String status;
    private String createdTime;
}
