package pro.tham.ai.agents.callbacks;

import com.google.adk.agents.LlmAgent;
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
        
        LlmAgent llmAgent = LlmAgent.builder()
                .name(NAME)
                .model(MODEL)
                .description("Personal agent to answer questions about the country and its capitals")
                .instruction("You are a helful agent")
                .build();
        
        AgentExecutor.execute(llmAgent, NAME, MODEL);
    }
}
