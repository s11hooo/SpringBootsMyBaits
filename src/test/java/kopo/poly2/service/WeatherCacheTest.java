package kopo.poly2.service;

import kopo.poly2.config.CacheConfig;
import kopo.poly2.controller.WeatherController;
import kopo.poly2.dto.WeatherDTO;
import kopo.poly2.service.impl.WeatherService;
import org.junit.jupiter.api.*;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

class WeatherCacheTest {
    static MockRestServiceServer server;
    @Configuration static class TestConfig {
        @Bean RestClient.Builder weatherRestClientBuilder() {
            RestClient.Builder builder = RestClient.builder();
            server = MockRestServiceServer.bindTo(builder).build();
            return builder;
        }
        @Bean WeatherService weatherService(RestClient.Builder builder) { return new WeatherService(builder, "test-key"); }
    }
    static final String RESPONSE = """
        {"timezone":"Asia/Seoul","current":{"dt":1792972800,"temp":21.5},
         "daily":[{"dt":1792972800,"sunrise":1792962000,"sunset":1793000000,
         "temp":{"day":22.1,"min":15.2,"max":24.8}}]}
        """;
    AnnotationConfigApplicationContext context;
    iWeatherService service;
    @BeforeEach void open() {
        context = new AnnotationConfigApplicationContext(CacheConfig.class, TestConfig.class);
        service = context.getBean(iWeatherService.class);
    }
    @AfterEach void close() { try { server.verify(); } finally { context.close(); } }
    void expectWeather() {
        server.expect(requestTo(containsString("units=metric")))
                .andRespond(withSuccess(RESPONSE, MediaType.APPLICATION_JSON));
    }
    @Test void sameAndNearbyCoordinatesCallApiOnlyOnce() throws Exception {
        expectWeather();
        WeatherDTO first = service.getWeather(37.54411,126.83571);
        assertEquals(21.5, first.getTemp());
        assertEquals(15.2, first.getDailyList().get(0).getMin());
        assertEquals("Asia/Seoul", first.getTimezone());
        assertFalse(first.getDt().isBlank());
        assertEquals("", first.getDailyList().get(0).getMoonrise());
        assertSame(first, service.getWeather(37.54411,126.83571));
        assertSame(first, service.getWeather(37.54412,126.83572));
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) { out.writeObject(first); }
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            WeatherDTO copy = (WeatherDTO) in.readObject();
            assertEquals(first.getTemp(), copy.getTemp());
            assertEquals(1, copy.getDailyList().size());
        }
    }
    @Test void differentCoordinatesMakeSeparateRequests() {
        expectWeather(); expectWeather();
        service.getWeather(37.544,126.836);
        service.getWeather(35.180,129.076);
    }
    @Test void failedApiResponseIsNotCached() {
        server.expect(requestTo(containsString("onecall"))).andRespond(withStatus(HttpStatus.UNAUTHORIZED));
        expectWeather();
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> service.getWeather(37.544,126.836));
        assertEquals(HttpStatus.BAD_GATEWAY, error.getStatusCode());
        assertEquals(21.5, service.getWeather(37.544,126.836).getTemp());
    }
    @Test void invalidCoordinatesAreRejectedBeforeApiCall() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> service.getWeather(91,0));
        assertThrows(IllegalArgumentException.class, () -> service.getWeather(Double.NaN,0));
        MockMvcBuilders.standaloneSetup(new WeatherController(service)).build()
                .perform(get("/weather/getWeather").param("lat","91").param("lon","0"))
                .andExpect(status().isBadRequest());
    }
    @Test void missingKeyIsReportedWithoutCallingApi() {
        WeatherService noKey = new WeatherService(RestClient.builder(), "");
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> noKey.getWeather(37.544,126.836));
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, error.getStatusCode());
    }
    @Test void malformedResponseIsNotCached() {
        server.expect(requestTo(containsString("onecall")))
                .andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));
        expectWeather();
        assertThrows(ResponseStatusException.class, () -> service.getWeather(37.544,126.836));
        assertEquals(21.5, service.getWeather(37.544,126.836).getTemp());
    }
}