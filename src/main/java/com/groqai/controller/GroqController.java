package com.groqai.controller;

import com.groqai.model.ChatRequest;
import com.groqai.model.ChatResponse;
import com.groqai.service.GroqService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/groq")
@Validated
public class GroqController {

    private static final Logger log = LoggerFactory.getLogger(GroqController.class);

    private final GroqService groqService;

    public GroqController(GroqService groqService) {
        this.groqService = groqService;
    }

    /**
     * POST /api/groq/chat
     *
     * Request body:
     * {
     *   "message": "What is Java 11?",
     *   "systemPrompt": "You are a helpful assistant.",  // optional
     *   "model": "llama3-70b-8192",                     // optional override
     *   "maxTokens": 512                                 // optional override
     * }
     */
    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        log.info("POST /chat - message: {}", request.getMessage());

        ChatResponse response = groqService.chat(request);

        if (!response.isSuccess()) {http://localhost:5173/
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
        }
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/groq/ask?message=YourQuestion
     * Quick one-liner to call the AI.
     */
    @GetMapping("/ask")
    public ResponseEntity<ChatResponse> ask(@RequestParam String message) {
        log.info("GET /ask - message: {}", message);

        ChatResponse response = groqService.chat(new ChatRequest(message));

        if (!response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * GET /api/groq/models
     * Returns a list of free models you can use.
     */
    @GetMapping("/models")
    public ResponseEntity<Map<String, Object>> models() {
        Map<String, Object> result = new HashMap<>();
        result.put("freeModels", new String[]{
            "llama-3.1-8b-instant",
            "llama-3.3-70b-versatile",
            "meta-llama/llama-4-scout-17b-16e-instruct",
            "meta-llama/llama-4-maverick-17b-128e-instruct",
            "qwen/qwen-3-32b"
        });
        result.put("note", "All models are FREE on Groq. Change model in application.properties or pass in request body.");
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/groq/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "groq-ai-springboot");
        status.put("info", "Using FREE Groq API");
        return ResponseEntity.ok(status);
    }
}
