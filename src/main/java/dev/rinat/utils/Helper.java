package dev.rinat.utils;

import dev.rinat.models.MeasurementDto;
import dev.rinat.models.SensorDto;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Random;

public class Helper {
    static final Random random;
    static final RestTemplate restTemplate;
    static final HttpHeaders httpHeaders;
    static final HttpEntity<Object> getEntity;

    static {
        random = new Random();
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        getEntity = new HttpEntity<>(httpHeaders);
    }

    public static ResponseEntity<String> restExchange(RestTemplate restTemplate, String url, HttpMethod method, HttpEntity<?> httpEntity) {
        try {
            return restTemplate.exchange(url, method, httpEntity, String.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static double getRandomValue(double minValue, double maxValue) {
        double randomNumber = minValue + (maxValue - minValue) * random.nextDouble();
        BigDecimal bd = new BigDecimal(randomNumber);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static boolean getRandomBoolean() {
        return random.nextBoolean();
    }

    public static SensorDto createSensor(String sensorDtoName, String url) {
        SensorDto sensorDto = new SensorDto(sensorDtoName);
        HttpEntity<SensorDto> sensorHttpEntity = new HttpEntity<>(sensorDto, httpHeaders);
        ResponseEntity<String> newSensorResponseEntity =
                Helper.restExchange(restTemplate, url, HttpMethod.POST, sensorHttpEntity);
        if (newSensorResponseEntity != null) {
            System.out.println("Response Status Code: " + newSensorResponseEntity.getStatusCode());
        }
        return sensorDto;
    }

    public static void addMeasurements(SensorDto sensorDto, int amount, double minValue, double maxValue, String url, boolean print) {
        for (int i = 0; i < amount; i++) {
            double randomValue = Helper.getRandomValue(minValue, maxValue);
            boolean randomRaining = Helper.getRandomBoolean();
            MeasurementDto measurementDto = new MeasurementDto(randomValue, randomRaining, sensorDto);
            HttpEntity<MeasurementDto> measurementHttpEntity = new HttpEntity<>(measurementDto, httpHeaders);
            ResponseEntity<String> newMeasurementResponseEntity =
                    Helper.restExchange(restTemplate, url, HttpMethod.POST, measurementHttpEntity);
            if (newMeasurementResponseEntity != null && print) {
                System.out.println("Response Status Code: " + newMeasurementResponseEntity.getStatusCode());
            }
        }
    }

    public static Integer getRainyDaysCount(String url) {
        ResponseEntity<String> rainyDaysCountResponseEntity =
                Helper.restExchange(restTemplate, url, HttpMethod.GET, getEntity);
        if (rainyDaysCountResponseEntity != null) {
            System.out.println("Response Status Code: " + rainyDaysCountResponseEntity.getStatusCode());
            return Integer.parseInt(Objects.requireNonNull(rainyDaysCountResponseEntity.getBody()));
        }
        return null;
    }
}
