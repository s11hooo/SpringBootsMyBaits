package kopo.poly2.service;

import kopo.poly2.dto.WeatherDTO;

public interface iWeatherService {
    WeatherDTO getWeather(double lat, double lon);
}