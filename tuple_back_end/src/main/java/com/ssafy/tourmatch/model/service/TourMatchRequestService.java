package com.ssafy.tourmatch.model.service;

import com.ssafy.tourmatch.model.TourMatchRequestDto;
import com.ssafy.tourmatch.model.dto.request.AcceptRejectMatchRequestDto;
import com.ssafy.tourmatch.model.dto.request.RequestMatchDto;

import java.util.List;

public interface TourMatchRequestService {
    int insert(RequestMatchDto requestMatchDto) throws Exception;

    int setStatus(AcceptRejectMatchRequestDto acceptRejectMatchRequestDto) throws Exception;

    int setRejectRestRequest(AcceptRejectMatchRequestDto acceptRejectMatchRequestDto) throws Exception;

    // 내 여행 계획을 매칭으로 등록하였을 때 신청자 목록을 불러오기 위함
    List<TourMatchRequestDto> getMyMatchRequestPeople(int memberId, int tourPlanId) throws Exception;

    // 다른 사람의 여행 계획에 같이가기를 눌렀을 때 응답을 가져오기 위함
    List<TourMatchRequestDto> getMyMatchRequestList(int partnerId) throws Exception;
}
