package com.ssafy.tourmatch.model.mapper;

import com.ssafy.tourmatch.model.TourMatchDto;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.List;

@Mapper
public interface TourMatchMapper {
    int insert(TourMatchDto tourMatchDto)throws SQLException;

    List<TourMatchDto> getMyMatchList(int memberId) throws SQLException;

    List<TourMatchDto> getAllMatch(int memberId) throws SQLException;

    int setMatched(int tourPlanId) throws SQLException;

}
