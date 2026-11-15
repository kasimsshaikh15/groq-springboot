package com.groqai.service;

import com.groqai.config.GroqApiProperties;
import com.groqai.model.ChatRequest;
import com.groqai.model.ChatResponse;
import com.groqai.model.GroqApiModels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroqService {

    private static final Logger log = LoggerFactory.getLogger(GroqService.class);

    private final RestTemplate restTemplate;
    private final GroqApiProperties props;

    public GroqService(RestTemplate restTemplate, GroqApiProperties props) {
        this.restTemplate = restTemplate;
        this.props = props;
    }

    /**
     * Send a message to Groq AI and return the response.
     */
    public ChatResponse chat(ChatRequest request) {
        log.debug("Sending message to Groq API. Model: {}, Message: {}",
                props.getModel(), request.getMessage());

        try {
            // Build messages list
            List<GroqApiModels.GroqMessage> messages = new ArrayList<>();

            // Add optional system prompt
            if (request.getSystemPrompt() != null && !request.getSystemPrompt().isBlank()) {
                messages.add(new GroqApiModels.GroqMessage("system", request.getSystemPrompt()));
            }

            // Add user message
            messages.add(new GroqApiModels.GroqMessage("user", request.getMessage()));

            // Build request body
            GroqApiModels.GroqRequestBody body = new GroqApiModels.GroqRequestBody();
            body.setModel(request.getModel() != null ? request.getModel() : props.getModel());
            body.setMessages(messages);
            body.setMax_tokens(request.getMaxTokens() != null ? request.getMaxTokens() : props.getMaxTokens());

            // Build headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + props.getKey());

            HttpEntity<GroqApiModels.GroqRequestBody> entity = new HttpEntity<>(body, headers);

            log.debug("Calling Groq API at: {}", props.getUrl());

            // Make HTTP POST call
            ResponseEntity<GroqApiModels.GroqResponseBody> response = restTemplate.exchange(
                    props.getUrl(),
                    HttpMethod.POST,
                    entity,
                    GroqApiModels.GroqResponseBody.class
            );

            GroqApiModels.GroqResponseBody responseBody = response.getBody();
            if (responseBody == null) {
                return ChatResponse.error("Empty response received from Groq API");
            }

            log.debug("Groq API response received. ID: {}", responseBody.getId());

            // Map usage
            ChatResponse.Usage usage = null;
            if (responseBody.getUsage() != null) {
                usage = new ChatResponse.Usage(
                        responseBody.getUsage().getPromptTokens(),
                        responseBody.getUsage().getCompletionTokens(),
                        responseBody.getUsage().getTotalTokens()
                );
            }

            return ChatResponse.success(
                    responseBody.getId(),
                    responseBody.extractText(),
                    responseBody.getModel(),
                    responseBody.extractFinishReason(),
                    usage
            );

        } catch (HttpClientErrorException e) {
            log.error("Client error from Groq API: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return ChatResponse.error("Client error " + e.getStatusCode() + ": " + e.getResponseBodyAsString());

        } catch (HttpServerErrorException e) {
            log.error("Server error from Groq API: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return ChatResponse.error("Server error " + e.getStatusCode() + ": " + e.getResponseBodyAsString());

        } catch (Exception e) {
            log.error("Unexpected error calling Groq API", e);
            return ChatResponse.error("Unexpected error: " + e.getMessage());
        }
    }
}
