package pro.tham.ai.agents.callbacks;

import com.google.adk.agents.LlmAgent;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import pro.tham.ai.agents.base.AgentExecutor;

/**
 *
 * @author Tham
 */
public class AgentWithModelCallback {
    private static final String NAME = "model-callback-agent";
    private static final String MODEL = "gemini-2.0-flash";
    
    public void run() {
        System.out.println("*** Agent with before model callback ***");
        
        LlmAgent llmAgent = LlmAgent.builder()
                .name(NAME)
                .model(MODEL)
                .description("Personal agent to answer questions about the country and its capitals")
                .instruction("You are a helpful agent")
                .beforeModelCallbackSync(AiCallbacks.beforeModel)
                .afterModelCallbackSync(AiCallbacks.afterModel)
                .build();
        
        System.out.println("Agent is about to be called");
        AgentExecutor.execute(llmAgent, NAME, MODEL, null, null);
    }
}
