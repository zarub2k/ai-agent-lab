package pro.tham.ai.agents.llm;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.sessions.Session;
import com.google.adk.tools.Annotations.Schema;
import com.google.adk.tools.FunctionTool;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Flowable;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Tham
 */
public class HelloJavaAgent {
    private static String USER_ID = "tham";
    private static String AGENT_NAME = "tham-ai-agent";
    private static String MODEL_NAME = "gemini-2.0-flash";
    
    public static BaseAgent ROOT_AGENT = initAgent();
    
    public static BaseAgent initAgent() {
        return LlmAgent.builder()
                .name(AGENT_NAME)
                .model(MODEL_NAME)
                .description("Personal agent to answer questions about the weather in a city")
                .instruction("""
                             You are a helpful agent who can answer user questions
                             about the weather in a city from the main agent.
                             """)
                .tools(FunctionTool.create(HelloJavaAgent.class, "getWeather"))
                .build();
    }
    
    public static Map<String, String> getWeather(@Schema(description = "The name of the city for which to retrieve the weather report")
            String city) {
        String weather = WeatherDataProvider.weather(city);
        if (weather != null) {
            return Map.of(
                    "status", "success",
                    "report", weather);
        } else {
            return Map.of(
                    "status", "error",
                    "report", "Weather information for " + city + " is not available.");
        }
    }

    public static void main(String[] args) {
        System.out.println("Hello Java AI Agent");
        InMemoryRunner runner = new InMemoryRunner(ROOT_AGENT);
        Session session = runner.sessionService()
                .createSession(AGENT_NAME, USER_ID)
                .blockingGet();

        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            while (true) {
                System.out.print("You >>>::");
                String userInput = scanner.nextLine();

                if ("quit".equalsIgnoreCase(userInput)) {
                    break;
                }

                Content userMsg = Content.fromParts(Part.fromText(userInput));
                Flowable<Event> events = runner.runAsync(USER_ID, session.id(), userMsg);

                System.out.print("Agent >>>::");
                events.blockingForEach(event -> System.out.println(event.stringifyContent()));
            }
        }
    }
}
