package dev.rinat.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.rinat.models.MeasurementDto;
import dev.rinat.models.SensorDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Helper {
    static final Random random;
    static final RestTemplate restTemplate;
    static final HttpHeaders httpHeaders;
    static final HttpEntity<Object> getEntity;
    static double value;

    static {
        random = new Random();
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        getEntity = new HttpEntity<>(httpHeaders);
        value = 0.0;
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
//        double randomNumber = minValue + (maxValue - minValue) * random.nextDouble();
//        BigDecimal bd = new BigDecimal(randomNumber);
//        bd = bd.setScale(1, RoundingMode.HALF_UP);
//        return bd.doubleValue();
        if (value > maxValue - 1) {
            value = maxValue - 1;
        } else if (value < minValue + 1) {
            value = minValue + 1;
        } else if (random.nextBoolean()) {
            value += 1.0;
        } else {
            value -= 1.0;
        }
        return value;
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
            System.out.println("CreateSensor Response Status Code: " + newSensorResponseEntity.getStatusCode());
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
                System.out.println("Measurements Response Status Code: " + newMeasurementResponseEntity.getStatusCode());
            }
        }
    }

    public static Integer getRainyDaysCount(String url) {
        ResponseEntity<String> rainyDaysCountResponseEntity =
                Helper.restExchange(restTemplate, url, HttpMethod.GET, getEntity);
        if (rainyDaysCountResponseEntity != null) {
            System.out.println("RainyDaysCount Response Status Code: " + rainyDaysCountResponseEntity.getStatusCode());
            return Integer.parseInt(Objects.requireNonNull(rainyDaysCountResponseEntity.getBody()));
        }
        return null;
    }

    public static String getMeasurements(String url) {
        ResponseEntity<String> measurementsResponseEntity =
                Helper.restExchange(restTemplate, url, HttpMethod.GET, getEntity);
        try {
            if (measurementsResponseEntity != null) {
                System.out.println("GetMeasurements Response Status Code: " + measurementsResponseEntity.getStatusCode());
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(measurementsResponseEntity.getBody());
                JsonNode contentNode = rootNode.path("content");
                return contentNode.toString();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static String getMeasurements(String url, int page, int size, String sort) {
        String pagesUrl = "?page=" + page + "&size=" + size + "&sort=" + sort;
        return getMeasurements(url + pagesUrl);
    }

    public static void displayChart(String measurements) {
        JSONArray jsonArray = new JSONArray(measurements);
        List<Integer> xData = new ArrayList<>();
        List<Double> yData = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            xData.add(i);
            yData.add(jsonObject.getDouble("value"));
        }

        XYChart chart = new XYChartBuilder().width(1000).height(600)
                .title("Temperature from Sensor").xAxisTitle("Time").yAxisTitle("Temperature").build();
        chart.addSeries("Sensor", xData, yData);

        new SwingWrapper<>(chart).displayChart();
    }
}
