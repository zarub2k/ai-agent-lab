package pro.tham.ai.agents.llm;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.Annotations;
import com.google.adk.tools.FunctionTool;
import java.util.Map;

/**
 *
 * @author Tham
 */
public class BasicCalculatorAgent {
    private static String USER_ID = "tham";
    private static String AGENT_NAME = "basic-calculator-agent";
    private static String MODEL_NAME = "gemini-2.0-flash";

    public static BaseAgent initAgent() {
        return LlmAgent.builder()
                .name(AGENT_NAME)
                .model(MODEL_NAME)
                .description("Personal agent to answer questions about the basic calculator functions like add, sub, multiply and divide")
                .instruction("""
                             You are a helpful agent who can answer user questions
                             about the basic calculator functions for the 2 given integers.
                             This agent helps the user to get the add, sub, multiply and divide functions
                             """)
                .tools(FunctionTool.create(BasicCalculatorAgent.class, "add"),
                        FunctionTool.create(BasicCalculatorAgent.class, "sub"),
                        FunctionTool.create(BasicCalculatorAgent.class, "multiply"),
                        FunctionTool.create(BasicCalculatorAgent.class, "divide"))
                .build();
    }
    
    public static Map<String, String> add(@Annotations.Schema(description = "the first integer") int a,
            @Annotations.Schema(description = "The second integer") int b) {
        int result = a + b;
        return Map.of(
                "status", "success",
                "report", String.valueOf(result)
        );
    }
    
    public static Map<String, String> sub(@Annotations.Schema(description = "the first integer") int a,
            @Annotations.Schema(description = "The second integer") int b) {
        int result = a - b;
        return Map.of(
                "status", "success",
                "report", String.valueOf(result)
        );
    }

    public static Map<String, String> multiply(@Annotations.Schema(description = "the first integer") int a,
            @Annotations.Schema(description = "The second integer") int b) {
        int result = a * b;
        return Map.of(
                "status", "success",
                "report", String.valueOf(result)
        );
    }
    
    public static Map<String, String> divide(@Annotations.Schema(description = "the first integer") int a,
            @Annotations.Schema(description = "The second integer") int b) {
        double result = a / b;
        return Map.of(
                "status", "success",
                "report", String.valueOf(result)
        );
    }
}
