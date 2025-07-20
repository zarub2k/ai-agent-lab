package pro.tham.ai.agents.callbacks;

import com.google.adk.agents.CallbackContext;
import com.google.adk.agents.Callbacks;
import com.google.adk.sessions.State;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Tham
 */
public final class AiCallbacks {
    public static Callbacks.BeforeAgentCallbackSync beforeAgent =
            (CallbackContext context) -> {
                System.out.println("Before agent: ");
                String agentName = context.agentName();
                State state = context.state();

                System.out.println("Agent name: " + agentName);
                System.out.println("Current state: " + state.entrySet());

                if (Boolean.TRUE.equals(state.get("skip_llm_agent"))) {
                    System.out.println("LLM agent call is skipped due the skip variable");
                    return Optional.of(
                            Content.fromParts(
                                    Part.fromText("Agent is skipped due to skip value")
                            ));
                }

                return Optional.empty();
            };
    
    public static Callbacks.AfterAgentCallbackSync afterAgent =
            (CallbackContext context) -> {
                System.out.println("After agent: ");
                
                State state = context.state();
                if (Boolean.TRUE.equals(state.get("add_concluding_note"))) {
                    return Optional.of(
                            Content.builder()
                            .parts(
                                    List.of(Part.fromText("Concluding note was added. Replacing original output with this one")))
                            .role("model")
                            .build()
                    );
                }
                
                return Optional.empty();
            };
}
