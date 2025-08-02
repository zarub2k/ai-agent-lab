package pro.tham.ai.agents.base;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Tham
 */
public class AiUtility {
    private static final String SEARCH_TERM = "joke";
    private static final String REPLACE_TERM = "funny story";
    private static final Pattern SEARCH_PATTERN
            = Pattern.compile("\\b" + Pattern.quote(SEARCH_TERM) + "\\b", Pattern.CASE_INSENSITIVE);
    
    public static String findAndReplace(String actual) {
        Matcher matcher = SEARCH_PATTERN.matcher(actual);
        if (!matcher.find()) {
            return "";
        }
        
        return matcher.replaceAll(REPLACE_TERM);
    }
    
    public static Map<String, String> countriesWithCapitals(){
        return Map.of(
                "india", "New Delhi",
                "england", "London",
                "usa", "Washington, D.C.",
                "canada", "Ottawa",
                "australia", "Canberra",
                "germany", "Berlin"
        );
    }
}
