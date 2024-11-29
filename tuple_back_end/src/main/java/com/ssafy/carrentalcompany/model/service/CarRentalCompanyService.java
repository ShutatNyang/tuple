package com.ssafy.carrentalcompany.model.service;

import com.ssafy.carrentalcompany.model.CarRentalCompanyDto;
import com.ssafy.carrentalcompany.model.dto.RequestByCodesDto;

import java.util.List;

public interface CarRentalCompanyService {
    List<CarRentalCompanyDto> listAll() throws Exception;

    List<CarRentalCompanyDto> listByAddress(String addressJibun) throws Exception;

    List<CarRentalCompanyDto> listByCodes(RequestByCodesDto requestByCodesDto) throws Exception;
}
