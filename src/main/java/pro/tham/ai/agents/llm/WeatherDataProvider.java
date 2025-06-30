package pro.tham.ai.agents.llm;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Tham
 */

// Dummy data provider. Use DB or external APIs in real use-cases
public class WeatherDataProvider {
    private static Map<String, String> weatherMap = new HashMap();
    static {
        weatherMap.put("Chennai", "Cloudy weather today");
        weatherMap.put("New York", "Sunny intervals and a gentle breeze");
        weatherMap.put("London", "Clear sky and gentle breeze");
        weatherMap.put("Berlin", "Clear sky and light winds");
        weatherMap.put("Delhi", "Heavy rain and organge alert");
        weatherMap.put("Sydney", "Mostly sunny morning");
    }
    
    public static String weather(String city) {
        String weather = weatherMap.get(city);
        if (weather == null || "".equals(weather)) {
            return "";
        }
        
        return weather;
    }
}
