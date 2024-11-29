package com.ssafy.tourmatch.model.service;

import com.ssafy.tourmatch.model.TourMatchDto;
import com.ssafy.tourmatch.model.mapper.TourMatchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourMatchServiceImpl implements TourMatchService {

    private final TourMatchMapper tourMatchMapper;

    @Autowired
    public TourMatchServiceImpl(TourMatchMapper tourMatchMapper) {
        this.tourMatchMapper = tourMatchMapper;
    }

    @Override
    public int insert(TourMatchDto tourMatchDto) throws Exception {
        return tourMatchMapper.insert(tourMatchDto);
    }

    @Override
    public List<TourMatchDto> getMyMatchList(int memberId) throws Exception {
        return tourMatchMapper.getMyMatchList(memberId);
    }

    @Override
    public List<TourMatchDto> getAllMatch(int memberId) throws Exception {
        return tourMatchMapper.getAllMatch(memberId);
    }

    @Override
    public int setMatched(int tourPlanId) throws Exception {
        return tourMatchMapper.setMatched(tourPlanId);
    }
}
