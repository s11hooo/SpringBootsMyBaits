package kopo.poly2.controller;

import kopo.poly2.dto.WeatherDTO;
import kopo.poly2.service.iWeatherService;
import kopo.poly2.utill.WeatherCoordinates;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {
    private final iWeatherService weatherService;
    @GetMapping("/getWeather")
    public WeatherDTO getWeather(@RequestParam double lat, @RequestParam double lon) {
        try { WeatherCoordinates.validate(lat, lon); }
        catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        return weatherService.getWeather(lat, lon);
    }
}