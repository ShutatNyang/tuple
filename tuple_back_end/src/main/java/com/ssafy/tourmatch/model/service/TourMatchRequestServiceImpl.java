package com.ssafy.tourmatch.model.service;

import com.ssafy.tourmatch.model.TourMatchRequestDto;
import com.ssafy.tourmatch.model.dto.request.AcceptRejectMatchRequestDto;
import com.ssafy.tourmatch.model.dto.request.RequestMatchDto;
import com.ssafy.tourmatch.model.mapper.TourMatchRequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourMatchRequestServiceImpl implements TourMatchRequestService {

    private final TourMatchRequestMapper tourMatchRequestMapper;

    @Autowired
    public TourMatchRequestServiceImpl(TourMatchRequestMapper tourMatchRequestMapper) {
        this.tourMatchRequestMapper = tourMatchRequestMapper;
    }

    @Override
    public int insert(RequestMatchDto requestMatchDto) throws Exception {
        return tourMatchRequestMapper.insert(requestMatchDto);
    }

    @Override
    public int setStatus(AcceptRejectMatchRequestDto acceptRejectMatchRequestDto) throws Exception {
        return tourMatchRequestMapper.setStatus(acceptRejectMatchRequestDto);
    }

    @Override
    public int setRejectRestRequest(AcceptRejectMatchRequestDto acceptRejectMatchRequestDto) throws Exception {
        return tourMatchRequestMapper.setRejectRestRequest(acceptRejectMatchRequestDto);
    }

    @Override
    public List<TourMatchRequestDto> getMyMatchRequestPeople(int memberId, int tourPlanId) throws Exception {
        return tourMatchRequestMapper.getMyMatchRequestPeople(memberId, tourPlanId);
    }

    @Override
    public List<TourMatchRequestDto> getMyMatchRequestList(int partnerId) throws Exception {
        return tourMatchRequestMapper.getMyMatchRequestList(partnerId);
    }
}
