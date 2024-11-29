package com.ssafy.carrentalcompany.model.service;

import com.ssafy.carrentalcompany.model.CarRentalCompanyDto;
import com.ssafy.carrentalcompany.model.dto.RequestByCodesDto;
import com.ssafy.carrentalcompany.model.mapper.CarRentalCompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CarRentalCompanyServiceImpl implements CarRentalCompanyService {

    private final CarRentalCompanyMapper carRentalCompanyMapper;

    @Autowired
    public CarRentalCompanyServiceImpl(CarRentalCompanyMapper carRentalCompanyMapper) {
        this.carRentalCompanyMapper = carRentalCompanyMapper;
    }

    @Override
    public List<CarRentalCompanyDto> listAll() throws Exception {
        List<CarRentalCompanyDto> result = carRentalCompanyMapper.listAll();

        if (result == null || result.isEmpty()) {
            throw new RuntimeException("No car rental companies found.");
        }

        return result;
    }

    @Override
    public List<CarRentalCompanyDto> listByAddress(String addressJibun) throws Exception {
        if (addressJibun == null || addressJibun.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty");
        }

        List<CarRentalCompanyDto> result = carRentalCompanyMapper.listByAddress(addressJibun);

        if (result == null || result.isEmpty()) {
            throw new RuntimeException("No car rental companies found.");
        }

        return result;
    }

    @Override
    public List<CarRentalCompanyDto> listByCodes(RequestByCodesDto requestByCodesDto) throws Exception {
        return carRentalCompanyMapper.listByCodes(requestByCodesDto);
    }


}
