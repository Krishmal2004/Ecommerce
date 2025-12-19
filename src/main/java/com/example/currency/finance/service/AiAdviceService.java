package com.example.currency.finance.service;

import com.example.currency.finance.dto.DashboardResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiAdviceService {

    @Value("${ai.api.key:NONE}")
    private String apiKey;

    // We use the Gemini 1.5 Flash model which is fast and free-tier eligible
    private static final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=";
    public String generateFinancialAdvice(DashboardResponse data) {
        // 1. Check if the user added a real key
        if ("NONE".equals(apiKey) || apiKey == null || apiKey.length() < 10) {
            return generateFallbackAdvice(data);
        }

        // 2. Build a prompt using REAL user data
        String prompt = String.format(
                "You are a financial advisor. Analyze this user's monthly finance stats:\n" +
                        "- Total Income: $%.2f\n" +
                        "- Total Expenses: $%.2f\n" +
                        "- Current Balance: $%.2f\n" +
                        "Based on this, give ONE single, short, encouraging sentence of advice (max 20 words) on how to save more money or manage debt.",
                data.getTotalIncome(),
                data.getTotalExpenses(),
                data.getTotalBalance()
        );

        // 3. Call the Real Google API
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Construct JSON Body: { "contents": [{ "parts": [{ "text": "..." }] }] }
            Map<String, Object> part = new HashMap<>();
            part.put("text", prompt);

            Map<String, Object> content = new HashMap<>();
            content.put("parts", Collections.singletonList(part));

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contents", Collections.singletonList(content));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // Send Request
            ResponseEntity<Map> response = restTemplate.postForEntity(GEMINI_URL + apiKey, entity, Map.class);

            // 4. Extract Response
            return extractTextFromGemini(response.getBody());

        } catch (Exception e) {
            // If the internet is down or key is wrong, print error and show fallback
            System.err.println("AI API Error: " + e.getMessage());
            return generateFallbackAdvice(data);
        }
    }

    // Parse the complex JSON response from Google
    private String extractTextFromGemini(Map<String, Object> responseBody) {
        try {
            if (responseBody == null) return "Financial analysis unavailable.";

            // Navigate: candidates[0] -> content -> parts[0] -> text
            List candidates = (List) responseBody.get("candidates");
            if (candidates == null || candidates.isEmpty()) return "No advice generated.";

            Map candidate = (Map) candidates.get(0);
            Map content = (Map) candidate.get("content");
            List parts = (List) content.get("parts");
            Map firstPart = (Map) parts.get(0);

            String aiText = (String) firstPart.get("text");
            return "✨ AI Insight: " + aiText.trim();

        } catch (Exception e) {
            return "Error parsing AI response.";
        }
    }

    // Fallback logic (used if API key is missing or internet fails)
    private String generateFallbackAdvice(DashboardResponse data) {
        if (data.getTotalBalance() < 0) {
            return "⚠️ Alert: Your expenses exceed your income. Review your recent transactions immediately.";
        } else if (data.getTotalExpenses() > data.getTotalIncome() * 0.9) {
            return "⚠️ Warning: You are spending over 90% of your income. Try to cut non-essential costs.";
        } else {
            return "✅ Great Job: You are maintaining a healthy balance. Consider investing your surplus.";
        }
    }
}