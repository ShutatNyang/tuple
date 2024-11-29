package com.ssafy.tourplan.model.mapper;

import com.ssafy.tourplan.model.TourPlanDto;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.List;

@Mapper
public interface TourPlanMapper {
    int insert (TourPlanDto tourPlanDto) throws SQLException;

    int delete (int id) throws SQLException;

    int update (TourPlanDto tourPlanDto) throws SQLException;

    TourPlanDto detail (int id) throws SQLException;

    List<TourPlanDto> listAll () throws SQLException;

    List<TourPlanDto> listByStartPosition(String startPosition) throws SQLException;

    String getEmailByTourPlanId(int id) throws SQLException;
}
