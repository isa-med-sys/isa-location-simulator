package com.isa.med_equipment_location_simulator;

import lombok.Data;

@Data
public class LocationDto {
    private Long userId;
    private Float longitude;
    private Float latitude;

    public LocationDto(Long uId, Float sLatitude, Float sLongitude) {
        userId = uId;
        latitude = sLatitude;
        longitude = sLongitude;
    }
}
