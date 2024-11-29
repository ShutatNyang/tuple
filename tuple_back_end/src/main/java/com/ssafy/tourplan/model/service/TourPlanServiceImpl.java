package com.ssafy.tourplan.model.service;

import com.ssafy.tourplan.model.TourPlanDto;
import com.ssafy.tourplan.model.mapper.TourPlanMapper;
import com.ssafy.trip.dto.response.SidosDto;
import com.ssafy.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourPlanServiceImpl implements TourPlanService {

    private final TourPlanMapper tourPlanMapper;

    @Autowired
    public TourPlanServiceImpl(TourPlanMapper tourPlanMapper) {
        this.tourPlanMapper = tourPlanMapper;
    }

    @Override
    public int insert(TourPlanDto tourPlanDto) throws Exception {
        // Null 값 및 기타 유효성 검증
        if (tourPlanDto == null || tourPlanDto.getOwnerId() == 0) {
            throw new IllegalArgumentException("유효하지 않은 TourPlan 데이터입니다.");
        }
        return tourPlanMapper.insert(tourPlanDto);
    }

    @Override
    public int delete(int id) throws Exception {
        TourPlanDto tourPlan = tourPlanMapper.detail(id);
        if (tourPlan == null) {
            throw new IllegalArgumentException("해당 ID의 TourPlan이 존재하지 않습니다.");
        }
        return tourPlanMapper.delete(id);
    }

    @Override
    public int update(TourPlanDto tourPlanDto) throws Exception {
        if (tourPlanDto == null || tourPlanDto.getId() == 0) {
            throw new IllegalArgumentException("유효하지 않은 업데이트 데이터입니다.");
        }
        return tourPlanMapper.update(tourPlanDto);
    }

    @Override
    public TourPlanDto detail(int id) throws Exception {
        TourPlanDto tourPlan = tourPlanMapper.detail(id);
        if (tourPlan == null) {
            throw new IllegalArgumentException("해당 ID의 TourPlan이 존재하지 않습니다.");
        }
        return tourPlan;
    }

    @Override
    public List<TourPlanDto> listAll() throws Exception {
        return tourPlanMapper.listAll();
    }

    @Override
    public List<TourPlanDto> listByStartPosition(String startPosition) throws Exception {
        return tourPlanMapper.listByStartPosition(startPosition);
    }

    @Override
    public void validateMemberAuthorization(String token, int id) throws Exception {
        if (!JwtUtil.validateToken(token)) {
            throw new SecurityException("유효하지 않은 토큰입니다.");
        }
        String jwtEmail = JwtUtil.getEmailFromToken(token);
        String tourPlanEmail = getEmailByTourPlanId(id);
        if (!jwtEmail.equals(tourPlanEmail)) {
            throw new SecurityException("권한이 없습니다.");
        }
    }


    private String getEmailByTourPlanId(int id) throws Exception {
        String email = tourPlanMapper.getEmailByTourPlanId(id);
        if (email == null) {
            throw new IllegalArgumentException("해당 Board ID에 대한 이메일이 존재하지 않습니다.");
        }
        return email;
    }
}

