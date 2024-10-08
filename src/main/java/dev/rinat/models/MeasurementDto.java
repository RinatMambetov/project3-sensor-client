package dev.rinat.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MeasurementDto {

    private double value;
    private boolean raining;
    private SensorDto sensorDto;
}
