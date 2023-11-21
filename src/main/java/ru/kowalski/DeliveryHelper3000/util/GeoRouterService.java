package ru.kowalski.DeliveryHelper3000.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;


@Component
@RequiredArgsConstructor

public class GeoRouterService {

    @Value("${geo.key}")
    private String geoToken;

    public String getRouteUrlWithGeoToken() {
        String routeUrl = "http://routing.api.2gis.com/routing/7.0.0/global?key=";
        return (routeUrl + geoToken);
    }

    // Метод отправляет JSON координаты склада и новой торговой точки пост-запросом и возвращает
    // в теле ответа время пути и расстояние при помощи 2gis-API
    public String[] getGeoRouteFromHTTP(Double stopPointLongitude, Double stopPointLatitude) throws JsonProcessingException {
        String body = "{\n" +
                "    \"points\": [\n" +
                "        {\n" +
                "            \"type\": \"stop\",\n" +
                "            \"lon\": " + DistanceCalculator.START_POINT_LONGITUDE + ",\n" +
                "            \"lat\": " + DistanceCalculator.START_POINT_LATITUDE + "\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\": \"stop\",\n" +
                "            \"lon\": " + stopPointLongitude + ",\n" +
                "            \"lat\": " + stopPointLatitude + "\n" +
                "        }\n" +
                "    ],\n" +
                "    \"transport\": \"car\",\n" +
                "    \"route_mode\": \"fastest\",\n" +
                "    \"traffic_mode\": \"jam\",\n" +
                "    \"output\": \"summary\"\n" +
                "}";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(getRouteUrlWithGeoToken(), HttpMethod.POST, entity, String.class);

        String responseBody = response.getBody();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(responseBody);

        String[] geoCode = new String[2];
        geoCode[0] = node.at("/result/0/duration").asText();
        geoCode[1] = node.at("/result/0/length").asText();

        return geoCode;
    }
}
