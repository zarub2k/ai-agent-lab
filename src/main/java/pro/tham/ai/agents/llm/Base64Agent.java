package pro.tham.ai.agents.llm;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.Annotations;
import com.google.adk.tools.FunctionTool;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 *
 * @author Tham
 */
public class Base64Agent {
    private static String USER_ID = "tham";
    private static String AGENT_NAME = "base64-agent";
    private static String MODEL_NAME = "gemini-2.0-flash";

    public static BaseAgent initAgent() {
        return LlmAgent.builder()
                .name(AGENT_NAME)
                .model(MODEL_NAME)
                .description("Personal agent to answer questions about the developers base64 utility for encode and decode")
                .instruction("""
                             You are a helpful agent who can answer user questions
                             about the base64 encode and decode functions.
                             1. encode is for encoding the given input
                             2. decode is for decoding the given input. Just s reverse of encode
                             """)
                .tools(FunctionTool.create(Base64Agent.class, "encode"), 
                        FunctionTool.create(Base64Agent.class, "decode"))
                .build();
    }
    
    public static Map<String, String> encode(@Annotations.Schema(description = "the source input") String input) {
        Base64.Encoder encoder = Base64.getEncoder();
        String result = encoder.encodeToString(input.getBytes(StandardCharsets.UTF_8));
        return Map.of(
                "status", "success",
                "report", String.valueOf(result)
        );
    }
    
    public static Map<String, String> decode(@Annotations.Schema(description = "the source input") String input) {
        Base64.Decoder decoder = Base64.getDecoder();
        String result = new String(decoder.decode(input));
        return Map.of(
                "status", "success",
                "report", String.valueOf(result)
        );
    }
}
