package com.groqai.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ChatRequest {

    @NotBlank(message = "Message must not be blank")
    @Size(min = 1, max = 10000, message = "Message must be between 1 and 10000 characters")
    private String message;

    private String systemPrompt;   // optional
    private String model;          // optional - override default model
    private Integer maxTokens;     // optional - override default max tokens

    public ChatRequest() {}
    public ChatRequest(String message) { this.message = message; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getSystemPrompt() { return systemPrompt; }
    public void setSystemPrompt(String systemPrompt) { this.systemPrompt = systemPrompt; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public Integer getMaxTokens() { return maxTokens; }
    public void setMaxTokens(Integer maxTokens) { this.maxTokens = maxTokens; }
}
