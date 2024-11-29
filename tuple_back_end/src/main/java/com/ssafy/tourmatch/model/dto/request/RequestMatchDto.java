package com.ssafy.tourmatch.model.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RequestMatchDto {
    private int memberId;
    private int partnerId;
    private int tourPlanId;
}
