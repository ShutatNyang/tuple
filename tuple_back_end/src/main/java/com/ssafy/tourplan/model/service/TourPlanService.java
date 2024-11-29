package com.ssafy.tourplan.model.service;


import com.ssafy.tourplan.model.TourPlanDto;
import com.ssafy.trip.dto.response.SidosDto;

import java.util.List;

public interface TourPlanService {
    int insert (TourPlanDto tourPlanDto) throws Exception;

    int delete(int id) throws Exception;

    int update (TourPlanDto tourPlanDto) throws Exception;

    TourPlanDto detail (int id) throws Exception;

    List<TourPlanDto> listAll () throws Exception;

    List<TourPlanDto> listByStartPosition(String startPosition) throws Exception;

    void validateMemberAuthorization(String token, int id) throws Exception;


}
