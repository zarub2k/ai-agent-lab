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
                             And you can also total two nmbers, use the add tool for that
                             """)
                .tools(FunctionTool.create(this.getClass(), "getCapitalCity"),
                        FunctionTool.create(this.getClass(), "add"))
                .beforeToolCallbackSync(AiCallbacks.beforeTool)
                .afterToolCallbackSync(AiCallbacks.afterTool)
                .build();

        AgentExecutor.execute(llmAgent, NAME, MODEL, null, null);
    }
    
    public static Map<String, Object> getCapitalCity(
            @Annotations.Schema(name="country", description = "The country to find the capital of")
                    String country) {
        System.out.println("getCapitalCity() tool is called");
        String capital = AiUtility.countriesWithCapitals()
                .getOrDefault(country.toLowerCase(), "Capital is not found for " + country);
        
        return Map.of("result", capital);
    }
    
    public static Map<String, Object> add(
            @Annotations.Schema(name = "num1", description = "The first number") int num1,
            @Annotations.Schema(name = "num2", description = "The second number") int num2) {
        System.out.println("add() tool is called");
        return Map.of("result", num1 + num2);
    }
}
