package pro.tham.ai.agents.callbacks;

import com.google.adk.agents.CallbackContext;
import com.google.adk.agents.Callbacks;
import com.google.adk.agents.LlmAgent;
import com.google.adk.models.LlmRequest;
import java.util.Optional;
import pro.tham.ai.agents.base.AgentExecutor;

/**
 *
 * @author Tham
 */
public class AgentWithBeforeModelCallback {
    private static final String NAME = "callback-agent";
    private static final String MODEL = "gemini-2.0-flash";
    
    public static void main(String[] args) {
        System.out.println("*** Agent with before model callback ***");
        
        Callbacks.BeforeModelCallbackSync beforeModelCallback = 
                (CallbackContext callbackContext, LlmRequest llmRequest) -> {
                    System.out.println("Callback is called before model....: " + callbackContext.agentName());
                    return Optional.empty();
                };
        
        LlmAgent llmAgent = LlmAgent.builder()
                .name(NAME)
                .model(MODEL)
                .description("Personal agent to answer questions about the country and its capitals")
                .instruction("You are a helful agent")
                .beforeModelCallbackSync(beforeModelCallback)
                .build();
        
        System.out.println("Agent is about to be called");
        AgentExecutor.execute(llmAgent, NAME, MODEL);
    }
}
