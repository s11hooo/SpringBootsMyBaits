package kopo.poly2.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import kopo.poly2.dto.WeatherDTO;
import kopo.poly2.dto.WeatherDailyDTO;
import kopo.poly2.service.iWeatherService;
import kopo.poly2.utill.WeatherCoordinates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class WeatherService implements iWeatherService {
    private final RestClient client;
    private final String apiKey;

    public WeatherService(RestClient.Builder builder, @Value("${weather.api.key:}") String apiKey) {
        this.client = builder.baseUrl("https://api.openweathermap.org/data/3.0/onecall").build();
        this.apiKey = apiKey;
    }

    @Override
    @Cacheable(cacheNames = "weather", keyGenerator = "latLonKeyGen", sync = true)
    public WeatherDTO getWeather(double lat, double lon) {
        WeatherCoordinates.validate(lat, lon);
        if (apiKey.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "날씨 API 키를 설정해 주세요. (WEATHER_API_KEY)");
        }
        log.info("Weather API request: lat={}, lon={}", WeatherCoordinates.round(lat), WeatherCoordinates.round(lon));
        JsonNode root;
        try {
            root = client.get().uri(uri -> uri
                    .queryParam("lat", WeatherCoordinates.round(lat))
                    .queryParam("lon", WeatherCoordinates.round(lon))
                    .queryParam("units", "metric").queryParam("lang", "kr")
                    .queryParam("exclude", "minutely,hourly,alerts")
                    .queryParam("appid", apiKey).build())
                    .retrieve().body(JsonNode.class);
        } catch (RestClientException ex) {
            // Never log the exception URL, which contains the API key.
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "날씨 API 호출에 실패했습니다. API 키와 One Call 이용 권한을 확인해 주세요.");
        }
        if (root == null || !root.path("current").path("temp").isNumber()
                || !root.path("daily").isArray()) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "날씨 API 응답 형식이 올바르지 않습니다.");
        }
        ZoneId zone;
        try { zone = ZoneId.of(root.path("timezone").asText("UTC")); }
        catch (java.time.DateTimeException ex) { zone = ZoneId.of("UTC"); }
        WeatherDTO dto = new WeatherDTO();
        dto.setTimezone(zone.getId());
        dto.setTemp(root.path("current").path("temp").doubleValue());
        dto.setDt(format(root.path("current").path("dt"), zone, "yyyy-MM-dd HH:mm:ss"));
        for (JsonNode item : root.path("daily")) {
            WeatherDailyDTO daily = new WeatherDailyDTO();
            daily.setDt(format(item.path("dt"), zone, "yyyy-MM-dd"));
            daily.setSunrise(format(item.path("sunrise"), zone, "HH:mm:ss"));
            daily.setSunset(format(item.path("sunset"), zone, "HH:mm:ss"));
            daily.setMoonrise(format(item.path("moonrise"), zone, "HH:mm:ss"));
            daily.setMoonset(format(item.path("moonset"), zone, "HH:mm:ss"));
            daily.setDay(item.path("temp").path("day").asDouble());
            daily.setMin(item.path("temp").path("min").asDouble());
            daily.setMax(item.path("temp").path("max").asDouble());
            dto.getDailyList().add(daily);
        }
        return dto;
    }

    private static String format(JsonNode value, ZoneId zone, String pattern) {
        if (!value.isNumber() || value.asLong() <= 0) return "";
        return DateTimeFormatter.ofPattern(pattern).withZone(zone)
                .format(Instant.ofEpochSecond(value.asLong()));
    }
}