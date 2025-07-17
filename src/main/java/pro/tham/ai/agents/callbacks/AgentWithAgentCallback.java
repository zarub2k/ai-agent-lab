package pro.tham.ai.agents.callbacks;

import com.google.adk.agents.CallbackContext;
import com.google.adk.agents.Callbacks;
import com.google.adk.agents.LlmAgent;
import com.google.adk.sessions.State;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Maybe;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import pro.tham.ai.agents.base.AgentExecutor;

/**
 *
 * @author Tham
 */
public class AgentWithAgentCallback {
    private static final String NAME = "agent-callback-agent";
    private static final String MODEL = "gemini-2.0-flash";
    
    Callbacks.BeforeAgentCallbackSync beforeAgent
            = (CallbackContext context) -> {
                System.out.println("Before agent: ");
                String agentName = context.agentName();
                State state = context.state();

                System.out.println("Agent name: " + agentName);
                System.out.println("Current state: " + state.entrySet());

                if (Boolean.TRUE.equals(state.get("skip_llm_agent"))) {
                    System.out.println("LLM agent call is skipped due the skip variable");
                    return Maybe.just(
                            Content.fromParts(
                                    Part.fromText("Agent is skipped due to skip value")
                            ));
                }

                return Maybe.empty();
            };
    
    public void run() {
        System.out.println("*** AgentWithAgentCallback.run() ***");

        //Before agent callback
        
        
        //After agent callback
        Callbacks.AfterAgentCallbackSync afterAgent = 
                (CallbackContext context) -> {
                    System.out.println("After agent: ");
                    return Optional.empty();
                };

        LlmAgent llmAgent = LlmAgent.builder()
                .name(NAME)
                .model(MODEL)
                .description("Personal agent to answer questions about the country and its capitals")
                .instruction("You are a helful agent")
                .beforeAgentCallbackSync(beforeAgent)
                .afterAgentCallbackSync(afterAgent)
                .build();

        AgentExecutor.execute(llmAgent, NAME, MODEL, null, null);
        
        AgentExecutor.execute(llmAgent, NAME, MODEL,
                new ConcurrentHashMap<>(Map.of("skip_llm_agent", true)), null);
    }
    
    private void beforeAgentCallback(CallbackContext context) {
    
    }
}
