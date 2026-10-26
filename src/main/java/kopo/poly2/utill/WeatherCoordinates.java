package kopo.poly2.utill;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class WeatherCoordinates {
    private WeatherCoordinates() { }
    public static void validate(double lat, double lon) {
        if (!Double.isFinite(lat) || !Double.isFinite(lon)
                || lat < -90 || lat > 90 || lon < -180 || lon > 180) {
            throw new IllegalArgumentException("위도는 -90~90, 경도는 -180~180 범위여야 합니다.");
        }
    }
    public static String round(double value) {
        return BigDecimal.valueOf(value).setScale(3, RoundingMode.HALF_UP).toPlainString();
    }
}