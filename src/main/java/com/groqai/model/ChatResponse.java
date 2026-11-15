package com.groqai.model;

public class ChatResponse {

    private String id;
    private String content;
    private String model;
    private String finishReason;
    private Usage usage;
    private boolean success;
    private String errorMessage;

    public static ChatResponse success(String id, String content, String model,
                                        String finishReason, Usage usage) {
        ChatResponse r = new ChatResponse();
        r.id = id;
        r.content = content;
        r.model = model;
        r.finishReason = finishReason;
        r.usage = usage;
        r.success = true;
        return r;
    }

    public static ChatResponse error(String errorMessage) {
        ChatResponse r = new ChatResponse();
        r.success = false;
        r.errorMessage = errorMessage;
        return r;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getFinishReason() { return finishReason; }
    public void setFinishReason(String finishReason) { this.finishReason = finishReason; }

    public Usage getUsage() { return usage; }
    public void setUsage(Usage usage) { this.usage = usage; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    // Nested Usage
    public static class Usage {
        private int promptTokens;
        private int completionTokens;
        private int totalTokens;

        public Usage() {}
        public Usage(int promptTokens, int completionTokens, int totalTokens) {
            this.promptTokens = promptTokens;
            this.completionTokens = completionTokens;
            this.totalTokens = totalTokens;
        }

        public int getPromptTokens() { return promptTokens; }
        public void setPromptTokens(int promptTokens) { this.promptTokens = promptTokens; }

        public int getCompletionTokens() { return completionTokens; }
        public void setCompletionTokens(int completionTokens) { this.completionTokens = completionTokens; }

        public int getTotalTokens() { return totalTokens; }
        public void setTotalTokens(int totalTokens) { this.totalTokens = totalTokens; }
    }
}
