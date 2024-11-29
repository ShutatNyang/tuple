package com.ssafy.tourmatch.model.mapper;

import com.ssafy.tourmatch.model.TourMatchRequestDto;
import com.ssafy.tourmatch.model.dto.request.AcceptRejectMatchRequestDto;
import com.ssafy.tourmatch.model.dto.request.RequestMatchDto;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.List;

@Mapper
public interface TourMatchRequestMapper {
    int insert(RequestMatchDto requestMatchDto) throws SQLException;

    int setStatus(AcceptRejectMatchRequestDto acceptRejectMatchRequestDto) throws SQLException;

    int setRejectRestRequest(AcceptRejectMatchRequestDto acceptRejectMatchRequestDto) throws Exception;

    // 내 여행 계획을 매칭으로 등록하였을 때 신청자 목록을 불러오기 위함
    List<TourMatchRequestDto> getMyMatchRequestPeople(int memberId, int tourPlanId) throws SQLException;

    // 다른 사람의 여행 계획에 같이가기를 눌렀을 때 그 리스트의 응답을 가져오기 위함
    List<TourMatchRequestDto> getMyMatchRequestList(int partnerId) throws SQLException;
}
