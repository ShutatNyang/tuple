package com.ssafy.tourmatch.model.service;

import com.ssafy.tourmatch.model.TourMatchDto;

import java.util.List;

public interface TourMatchService {
    int insert(TourMatchDto tourMatchDto) throws Exception;

    List<TourMatchDto> getMyMatchList(int memberId) throws Exception;

    List<TourMatchDto> getAllMatch(int memberId) throws Exception;

    int setMatched(int tourPlanId) throws Exception;
}
