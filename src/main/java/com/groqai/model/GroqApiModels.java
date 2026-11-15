package com.groqai.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Internal models matching Groq's OpenAI-compatible /v1/chat/completions API.
 */
public class GroqApiModels {

    // ---- Outgoing request ----

    public static class GroqRequestBody {
        private String model;
        private List<GroqMessage> messages;
        private int max_tokens;
        private double temperature = 0.7;

        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }

        public List<GroqMessage> getMessages() { return messages; }
        public void setMessages(List<GroqMessage> messages) { this.messages = messages; }

        public int getMax_tokens() { return max_tokens; }
        public void setMax_tokens(int max_tokens) { this.max_tokens = max_tokens; }

        public double getTemperature() { return temperature; }
        public void setTemperature(double temperature) { this.temperature = temperature; }
    }

    public static class GroqMessage {
        private String role;    // "system", "user", "assistant"
        private String content;

        public GroqMessage() {}
        public GroqMessage(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    // ---- Incoming response ----

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GroqResponseBody {
        private String id;
        private String model;
        private List<Choice> choices;
        private GroqUsage usage;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }

        public List<Choice> getChoices() { return choices; }
        public void setChoices(List<Choice> choices) { this.choices = choices; }

        public GroqUsage getUsage() { return usage; }
        public void setUsage(GroqUsage usage) { this.usage = usage; }

        /** Extract first choice text */
        public String extractText() {
            if (choices == null || choices.isEmpty()) return "";
            Choice first = choices.get(0);
            if (first.getMessage() != null) return first.getMessage().getContent();
            return "";
        }

        /** Extract finish reason */
        public String extractFinishReason() {
            if (choices == null || choices.isEmpty()) return "";
            return choices.get(0).getFinishReason();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice {
        private int index;
        private GroqMessage message;

        @JsonProperty("finish_reason")
        private String finishReason;

        public int getIndex() { return index; }
        public void setIndex(int index) { this.index = index; }

        public GroqMessage getMessage() { return message; }
        public void setMessage(GroqMessage message) { this.message = message; }

        public String getFinishReason() { return finishReason; }
        public void setFinishReason(String finishReason) { this.finishReason = finishReason; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GroqUsage {
        @JsonProperty("prompt_tokens")
        private int promptTokens;

        @JsonProperty("completion_tokens")
        private int completionTokens;

        @JsonProperty("total_tokens")
        private int totalTokens;

        public int getPromptTokens() { return promptTokens; }
        public void setPromptTokens(int promptTokens) { this.promptTokens = promptTokens; }

        public int getCompletionTokens() { return completionTokens; }
        public void setCompletionTokens(int completionTokens) { this.completionTokens = completionTokens; }

        public int getTotalTokens() { return totalTokens; }
        public void setTotalTokens(int totalTokens) { this.totalTokens = totalTokens; }
    }
}
