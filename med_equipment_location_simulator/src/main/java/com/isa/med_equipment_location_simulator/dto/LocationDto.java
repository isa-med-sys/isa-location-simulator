package com.isa.med_equipment_location_simulator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationDto {
    private Long companyId;
    private Float longitude;
    private Float latitude;
}
