package pro.tham.ai.agents.base;

import com.google.adk.agents.BaseAgent;
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
public class AgentExecutor {
    public static void execute(BaseAgent agent, String agentName, String userId) {
        System.out.println("AgentExecutor.execute()");
        InMemoryRunner runner = new InMemoryRunner(agent);

        Session session = runner.sessionService()
                .createSession(agentName, userId)
                .blockingGet();

        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            while (true) {
                System.out.print("You >>>::");
                String userInput = scanner.nextLine();

                if ("quit".equalsIgnoreCase(userInput)) {
                    break;
                }

                Content userMsg = Content.fromParts(Part.fromText(userInput));
                Flowable<Event> events = runner.runAsync(userId, session.id(), userMsg);

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
            }
        }
    }
    
}
