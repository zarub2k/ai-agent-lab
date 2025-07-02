package pro.tham.ai.agents.callbacks;

import com.google.adk.agents.CallbackContext;
import com.google.adk.agents.Callbacks;
import com.google.adk.agents.LlmAgent;
import java.util.Optional;
import pro.tham.ai.agents.base.AgentExecutor;

/**
 *
 * @author Tham
 */
public class AgentWithAgentCallback {
    private static final String NAME = "agent-callback-agent";
    private static final String MODEL = "gemini-2.0-flash";
    
    public void run() {
        System.out.println("*** AgentWithAgentCallback.run() ***");

        Callbacks.BeforeAgentCallbackSync beforeAgent = 
                (CallbackContext context) -> {
                    System.out.println("Before agent: ");
                    return Optional.empty();
                };
        
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

        AgentExecutor.execute(llmAgent, NAME, MODEL);
    }
}
