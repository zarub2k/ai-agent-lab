package pro.tham.ai.agents.callbacks;

import com.google.adk.agents.LlmAgent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
        
        LlmAgent llmAgent = LlmAgent.builder()
                .name(NAME)
                .model(MODEL)
                .description("Personal agent to answer questions about the country and its capitals")
                .instruction("You are a helpful agent")
                .beforeAgentCallbackSync(AiCallbacks.beforeAgent)
                .afterAgentCallbackSync(AiCallbacks.afterAgent)
                .build();

        AgentExecutor.execute(llmAgent, NAME, MODEL, null, null);
//        
//        AgentExecutor.execute(llmAgent, NAME, MODEL,
//                new ConcurrentHashMap<>(Map.of("skip_llm_agent", true)), null);
        
//        AgentExecutor.execute(llmAgent, NAME, MODEL,
//                new ConcurrentHashMap<>(Map.of("add_concluding_note", true)), null);

//        AgentExecutor.execute(llmAgent, NAME, MODEL,
//                new ConcurrentHashMap<>(Map.of("skip_llm_agent", true, "add_concluding_note", true)), null);
    }
}
