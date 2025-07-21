package pro.tham.ai.agents.callbacks;

import com.google.adk.agents.CallbackContext;
import com.google.adk.agents.Callbacks;
import com.google.adk.models.LlmRequest;
import com.google.adk.models.LlmResponse;
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
                            buildContent("model", "Agent is skipped due to skip value"));
                            
                }

                return Optional.empty();
            };
    
    public static Callbacks.AfterAgentCallbackSync afterAgent =
            (CallbackContext context) -> {
                System.out.println("After agent: ");
                
                State state = context.state();
                if (Boolean.TRUE.equals(state.get("add_concluding_note"))) {
                    return Optional.of(
                            buildContent("model", 
                                    "Concluding note was added. Replacing original output with this one"));
                }
                
                return Optional.empty();
            };
    
    
    public static Callbacks.BeforeModelCallbackSync beforeModel
            = (CallbackContext callbackContext, LlmRequest llmRequest) -> {
                System.out.println("Callback before model: " + callbackContext.agentName());
                
                String userMessage = "";
                List<Content> contents = llmRequest.contents();
                if (contents != null || !contents.isEmpty()) {
                    userMessage = contents.getLast().parts().get().get(0).text().orElse("");
                }
                
                System.out.println("Inspecting last user message: " + userMessage);
                if (userMessage.toUpperCase().contains("ISS")) {
                    return Optional.of(
                            buildResponse("model", "LLM call is blocked by the before model callback"));
                }
                
                return Optional.empty();
            };

    public static Callbacks.AfterModelCallbackSync afterModel
            = (CallbackContext callbackContext, LlmResponse llmResponse) -> {
                System.out.println("Callback after model: " + callbackContext.agentName());
                return Optional.empty();
            };
    
    public static Content buildContent(String role, String value) {
        return Content.builder()
                .parts(List.of(Part.fromText(value)))
                .role(role)
                .build();
    }
    
    static LlmResponse buildResponse(String role, String value) {
        return LlmResponse
                .builder()
                .content(buildContent(role, value))
                .build();
    }
}
