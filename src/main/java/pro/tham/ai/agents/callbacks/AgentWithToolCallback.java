package pro.tham.ai.agents.callbacks;

import com.google.adk.agents.Callbacks;
import com.google.adk.agents.LlmAgent;
import java.util.Optional;
import pro.tham.ai.agents.base.AgentExecutor;

/**
 *
 * @author Tham
 */
public class AgentWithToolCallback {
    private static final String NAME = "tool-callback-agent";
    private static final String MODEL = "gemini-2.0-flash";

    public void run() {
        System.out.println("*** AgentWithToolCallback.run() ***");

        Callbacks.BeforeToolCallbackSync beforeTool =
                (invocationContext, baseTool, input, toolContext) -> {
                    return Optional.empty();
                };

        Callbacks.AfterToolCallbackSync afterTool = 
                (invocationContext, baseTool, input, toolContext, response) -> {
                    System.out.println("After tool");
                    return Optional.empty();
                };
                

        LlmAgent llmAgent = LlmAgent.builder()
                .name(NAME)
                .model(MODEL)
                .description("Personal agent to answer questions about the country and its capitals")
                .instruction("You are a helful agent")
                .beforeToolCallbackSync(beforeTool)
                .afterToolCallbackSync(afterTool)
                .build();

        AgentExecutor.execute(llmAgent, NAME, MODEL, null, null);
    }
}
