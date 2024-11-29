package com.ssafy.trip.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ContentTypeDto {
    private int contentTypeId;         // PRIMARY KEY
    private String contentTypeName;   // NULL 허용
}
