package ru.kowalski.DeliveryHelper3000.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GeoCoderService {

    @Value("${geo.key}")
    private String geoToken;

    private final String geoUrl = "https://catalog.api.2gis.com/3.0/items/geocode?q=";

    public String getGeoToken() {
        return ("&fields=items.point&key=" + geoToken);
    }

    // Метод отправляет гет-запрос с ключём 2gis-API и адресом новой торговой точки и возвращает её координаты
    // и полный адрес в формате 2гис
    public String[] getGeoCodeFromHTTP(String address) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(geoUrl + address + getGeoToken(), String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());
        String[] geoCode = new String[3];
        geoCode[0] = root.at("/result/items/0/full_name").asText();
        geoCode[1] = root.at("/result/items/0/point/lat").asText();
        geoCode[2] = root.at("/result/items/0/point/lon").asText();
        return geoCode;
    }

}
