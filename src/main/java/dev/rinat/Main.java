package dev.rinat;

import dev.rinat.models.SensorDto;
import dev.rinat.utils.Helper;


public class Main {

    public static void main(String[] args) {
        double minValue = -100.0;
        double maxValue = 100.0;

        String serverUrl = "http://localhost:8080";
        String sensorRegUrl = "/sensors/registration";
        String measurementAddUrl = "/measurements/add";
        String rainyDaysCountUrl = "/measurements/rainyDaysCount";
        String measurementsUrl = "/measurements";

        SensorDto sensorDto = Helper.createSensor("my_sensor", serverUrl + sensorRegUrl);

        Helper.addMeasurements(sensorDto, 1000, minValue, maxValue, serverUrl + measurementAddUrl, false);

        Integer rainyDaysCount = Helper.getRainyDaysCount(serverUrl + rainyDaysCountUrl);
        System.out.println("Rainy Days Count: " + rainyDaysCount);

        String measurements = Helper.getMeasurements(serverUrl + measurementsUrl);
//        System.out.println("Measurements: " + measurements);

        String pageOfMeasurements = Helper.getMeasurements(serverUrl + measurementsUrl, 1, 5, "value,desc");
        System.out.println("Page of Measurements: " + pageOfMeasurements);

        Helper.displayChart(measurements);
    }
}
