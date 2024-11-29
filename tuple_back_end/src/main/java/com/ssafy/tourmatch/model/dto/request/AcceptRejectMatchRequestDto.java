package com.ssafy.tourmatch.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcceptRejectMatchRequestDto {
    private int memberId;
    private int partnerId;
    private int tourPlanId;
    private String Status;
}
