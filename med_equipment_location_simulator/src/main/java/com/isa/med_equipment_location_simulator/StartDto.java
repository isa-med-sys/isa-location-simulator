package com.isa.med_equipment_location_simulator;

import lombok.Data;

@Data
public class StartDto {
    private Long userId;
    private Float longitudeStart;
    private Float latitudeStart;
    private Float longitudeEnd;
    private Float latitudeEnd;
}
