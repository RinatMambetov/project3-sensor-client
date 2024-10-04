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
        String pagesUrl = "?page=0&size=2&sort=value,asc";

        SensorDto sensorDto = Helper.createSensor("my_sensor", serverUrl + sensorRegUrl);

        Helper.addMeasurements(sensorDto, 10, minValue, maxValue, serverUrl + measurementAddUrl, false);

        Integer rainyDaysCount = Helper.getRainyDaysCount(serverUrl + rainyDaysCountUrl);
        System.out.println("Rainy Days Count: " + rainyDaysCount);

//        ResponseEntity<String> measurementsResponseEntity =
//                Helper.restExchange(restTemplate, serverUrl + measurementsUrl + pagesUrl, HttpMethod.GET, getEntity);
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            if (measurementsResponseEntity != null) {
//                System.out.println("Response Status Code: " + measurementsResponseEntity.getStatusCode());
//                JsonNode rootNode = objectMapper.readTree(measurementsResponseEntity.getBody());
//                JsonNode contentNode = rootNode.path("content");
//                System.out.println("Content: " + contentNode.toString());
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
    }


}
