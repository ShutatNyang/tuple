package com.ssafy.tourmatch.model;

import lombok.Getter;
import lombok.ToString;
import org.springframework.stereotype.Service;

@Getter
@Service
@ToString
public class TourMatchDto {
    private int id;
    private int memberId;
    private int tourPlanId;
    private String status;
    private String createdTime;
}
