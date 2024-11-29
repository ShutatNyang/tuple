package com.ssafy.carrentalcompany.model.mapper;

import com.ssafy.carrentalcompany.model.CarRentalCompanyDto;
import com.ssafy.carrentalcompany.model.dto.RequestByCodesDto;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.List;

@Mapper
public interface CarRentalCompanyMapper {
    List<CarRentalCompanyDto> listAll() throws SQLException;

    List<CarRentalCompanyDto> listByAddress(String addressJibun) throws SQLException;

    List<CarRentalCompanyDto> listByCodes(RequestByCodesDto requestByCodesDto) throws Exception;
}
