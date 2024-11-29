package com.ssafy.carrentalcompany.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CarRentalCompanyDto {
    private int id; // bigint corresponds to long
    private String name;
    private double latitude; // decimal(10,7) corresponds to double
    private double longitude; // decimal(10,7) corresponds to double
    private String contactInfo;
    private String website;
    private String addressRoad;
    private String addressJibun;
    private String garageAddressRoad;
    private String garageAddressJibun;
}
