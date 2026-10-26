package kopo.poly2.dto;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class WeatherDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private double temp;
    private String dt;
    private String timezone;
    private List<WeatherDailyDTO> dailyList = new ArrayList<>();
}