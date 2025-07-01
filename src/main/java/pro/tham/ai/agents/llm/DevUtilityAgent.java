package pro.tham.ai.agents.llm;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Flowable;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Scanner;

/**
 *
 * @author Tham
 */
public class DevUtilityAgent {
    private static String USER_ID = "tham";
    private static String AGENT_NAME = "dev-utility-agent";
    private static String MODEL_NAME = "gemini-2.0-flash";

    public static BaseAgent ROOT_AGENT = initAgent();

    public static BaseAgent initAgent() {
        return LlmAgent.builder()
                .name(AGENT_NAME)
                .model(MODEL_NAME)
                .description("Personal agent to answer questions about the developers daily utility")
                .instruction("""
                             You are a helpful agent who can answer user questions
                             about the developer utility.
                             You have a specialized sub agents for doing calculator and base64 operations
                             1. BasicCalculatorAgent: This is to provide sum, multiplication, subtraction and division
                             2. Base64Agent: This agent is to provide the base64 encode and decode functions
                             """)
                .subAgents(BasicCalculatorAgent.initAgent(), Base64Agent.initAgent())
                .build();
    }
    
    public static void main(String[] args) {
        System.out.println("Dev Utility Agent");
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
                events.blockingForEach(event -> {
                    if (event.finalResponse() && event.content().isPresent()) {
                        event
                                .content()
                                .get()
                                .parts()
                                .flatMap(parts -> parts.isEmpty() ? Optional.empty() : Optional.of(parts.get(0)))
                                .flatMap(Part::text)
                                .ifPresent(text -> System.out.println(text));
                    }
                });
//                events.blockingForEach(event -> System.out.println(event.stringifyContent()));
            }
        }
    }
}
