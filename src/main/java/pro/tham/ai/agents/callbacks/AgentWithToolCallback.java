package pro.tham.ai.agents.callbacks;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.Annotations;
import com.google.adk.tools.FunctionTool;
import java.util.Map;
import pro.tham.ai.agents.base.AgentExecutor;
import pro.tham.ai.agents.base.AiUtility;

/**
 *
 * @author Tham
 */
public class AgentWithToolCallback {
    private static final String NAME = "tool-callback-agent";
    private static final String MODEL = "gemini-2.0-flash";

    public void run() {
        System.out.println("*** AgentWithToolCallback.run() ***");
        LlmAgent llmAgent = LlmAgent.builder()
                .name(NAME)
                .model(MODEL)
                .description("You are a helpful agent")
                .instruction("""
                             You are an agent that can find capital cities.
                             Use the getCapitalCity tool.
                             """)
                .tools(FunctionTool.create(this.getClass(), "getCapitalCity"))
                .beforeToolCallbackSync(AiCallbacks.beforeTool)
                .afterToolCallbackSync(AiCallbacks.afterTool)
                .build();

        AgentExecutor.execute(llmAgent, NAME, MODEL, null, null);
    }
    
    public static Map<String, Object> getCapitalCity(
            @Annotations.Schema(name="country", description = "The country to find the capital of")
                    String country) {
        String capital = AiUtility.countriesWithCapitals()
                .getOrDefault(country.toLowerCase(), "Capital not found for " + country);
        
        return Map.of(country, capital);
    }
}
