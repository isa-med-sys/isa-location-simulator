package com.isa.med_equipment_location_simulator.dto;

import lombok.Data;

@Data
public class StartDto {
    private Long companyId;
    private Float longitudeStart;
    private Float latitudeStart;
    private Float longitudeEnd;
    private Float latitudeEnd;
}
