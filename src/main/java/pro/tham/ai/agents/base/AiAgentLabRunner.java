package pro.tham.ai.agents.base;

import pro.tham.ai.agents.callbacks.AgentWithToolCallback;

/**
 *
 * @author Tham
 */
public class AiAgentLabRunner {
    public static void main(String[] args) {
        System.out.println("Ai agent lab runner");
        
        new AgentWithToolCallback().run();
    }
}
