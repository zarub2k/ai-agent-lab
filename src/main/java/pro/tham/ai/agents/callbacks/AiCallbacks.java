package pro.tham.ai.agents.callbacks;

import com.google.adk.agents.CallbackContext;
import com.google.adk.agents.Callbacks;
import com.google.adk.models.LlmRequest;
import com.google.adk.models.LlmResponse;
import com.google.adk.sessions.State;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import pro.tham.ai.agents.base.AiUtility;

/**
 *
 * @author Tham
 */
public final class AiCallbacks {
    public static Callbacks.BeforeAgentCallbackSync beforeAgent =
            (CallbackContext context) -> {
                System.out.println("Before agent: ");
                String agentName = context.agentName();
                State state = context.state();

                System.out.println("Agent name: " + agentName);
                System.out.println("Current state: " + state.entrySet());

                if (Boolean.TRUE.equals(state.get("skip_llm_agent"))) {
                    System.out.println("LLM agent call is skipped due the skip variable");
                    return Optional.of(
                            buildContent("model", "Agent is skipped due to skip value"));
                            
                }

                return Optional.empty();
            };
    
    public static Callbacks.AfterAgentCallbackSync afterAgent =
            (CallbackContext context) -> {
                System.out.println("After agent: ");
                
                State state = context.state();
                if (Boolean.TRUE.equals(state.get("add_concluding_note"))) {
                    return Optional.of(
                            buildContent("model", 
                                    "Agent response is overwritten by the after agent callback!!!"));
                }
                
                return Optional.empty();
            };
    
    
    public static Callbacks.BeforeModelCallbackSync beforeModel
            = (CallbackContext callbackContext, LlmRequest llmRequest) -> {
                System.out.println("Callback before model: " + callbackContext.agentName());
                
                String userMessage = "";
                List<Content> contents = llmRequest.contents();
                if (contents != null || !contents.isEmpty()) {
                    userMessage = contents.getLast().parts().get().get(0).text().orElse("");
                }
                
                System.out.println("Inspecting last user message::: " + userMessage);
                if (userMessage.toUpperCase().contains("ISS")) {
                    return Optional.of(
                            buildResponse("model", "LLM call is blocked by the before model callback"));
                }
                
                return Optional.empty();
            };

    public static Callbacks.AfterModelCallbackSync afterModel =
            (CallbackContext callbackContext, LlmResponse llmResponse) -> {
                System.out.println("Callback after model: " + callbackContext.agentName());
                
                Content originalContent = llmResponse.content().get();
                
                String originalText = originalContent.parts().get().getLast().text().get();
                
                System.out.println("originalText: " + originalText);
                System.out.println("---->");
                String modifiedText = AiUtility.findAndReplace(originalText);
                if (!"".equals(modifiedText)) {
                    return Optional.of(
                        LlmResponse
                            .builder()
                            .content(originalContent.toBuilder()
                                    .parts(List.of(Part.fromText(modifiedText))).build())
                            .groundingMetadata(llmResponse.groundingMetadata())
                            .build());
                }
                
                return Optional.empty();
            };
    
    public static Callbacks.BeforeToolCallbackSync beforeTool =
            (invocationContext, baseTool, input, toolContext) -> {
                System.out.println("Callback before tool: " + invocationContext.agent().name());
                System.out.println("Tool name: " + baseTool.name());
                System.out.println("Input: " + input);
                
                if ("getCapitalCity".equals(baseTool.name()) && input.get("country").equals("chad")) {
                    return Optional.of(Map.of("result", "Tool execution is blocked :("));
                }
                
                return Optional.empty();
            };
    
    public static Callbacks.AfterToolCallbackSync afterTool =
            (invocationContext, baseTool, input, toolContext, response) -> {
                System.out.println("Callback after tool: " + invocationContext.agent().name());
                System.out.println("Input after: " + input);
                System.out.println("Actual response: " + response.toString());
                
                if (response instanceof Map value) {
                    Map modifiedResponse = new HashMap<String, Object>(value);
//                    String capital = (String) value.get(input.get("country"));
                    Object result = value.get("result");
                    if ("getCapitalCity".equals(baseTool.name()) && result instanceof String capital && "Berlin".equalsIgnoreCase(capital)) {
                        System.out.println("Condition met....");
//                        
                        modifiedResponse.put("result", capital + "(note) modified by after tool callback");
                        modifiedResponse.put("note_added_by_callback", true);
                        
                        System.out.println(modifiedResponse);
                        
                        return Optional.of(modifiedResponse);

                        
//                        return Optional.of(
//                                Map.of((String) input.get("country") + ">>", "::>" + capital + "<::"));
                    }
                    
                    if ("add".equals(baseTool.name()) && result instanceof Integer total && total.intValue() == 5) {
                        System.out.println("Condition met....");
//                        
                        modifiedResponse.put("result", total + "(note) modified by after tool callback");
                        modifiedResponse.put("note_added_by_callback", true);

                        System.out.println(modifiedResponse);

                        return Optional.of(modifiedResponse);

//                        return Optional.of(
//                                Map.of((String) input.get("country") + ">>", "::>" + capital + "<::"));
                    }
                }
                
                return Optional.empty();
            };
    
    public static Content buildContent(String role, String value) {
        return Content.builder()
                .parts(List.of(Part.fromText(value)))
                .role(role)
                .build();
    }
    
    static LlmResponse buildResponse(String role, String value) {
        return LlmResponse
                .builder()
                .content(buildContent(role, value))
                .build();
    }
}
