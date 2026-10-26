package kopo.poly2.dto;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public class WeatherDailyDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String dt;
    private String sunrise;
    private String sunset;
    private String moonrise;
    private String moonset;
    private double day;
    private double min;
    private double max;
}