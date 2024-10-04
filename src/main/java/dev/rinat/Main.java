package dev.rinat;

import dev.rinat.models.MeasurementDto;
import dev.rinat.models.SensorDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Main {
    public static void main(String[] args) {

        String sensorName = "my_sensor";
        String serverUrl = "http://localhost:8080";
        String sensorRegUrl = "/sensors/registration";
        String measurementAddUrl = "/measurements/add";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        RestTemplate restTemplate = new RestTemplate();
        SensorDto sensorDto = new SensorDto(sensorName);
        HttpEntity<SensorDto> sensorHttpEntity = new HttpEntity<>(sensorDto, httpHeaders);

        try {
            ResponseEntity<String> responseEntity =
                    restTemplate.exchange(serverUrl + sensorRegUrl, HttpMethod.POST, sensorHttpEntity, String.class);
            System.out.println("Response Status Code: " + responseEntity.getStatusCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        MeasurementDto measurementDto = new MeasurementDto(11.2, true, sensorDto);
        HttpEntity<MeasurementDto> measurementHttpEntity = new HttpEntity<>(measurementDto, httpHeaders);

        try {
            ResponseEntity<String> responseEntity =
                    restTemplate.exchange(serverUrl + measurementAddUrl, HttpMethod.POST, measurementHttpEntity, String.class);
            System.out.println("Response Status Code: " + responseEntity.getStatusCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
