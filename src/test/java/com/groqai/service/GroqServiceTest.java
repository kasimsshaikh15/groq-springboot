package com.groqai.service;

import com.groqai.config.GroqApiProperties;
import com.groqai.model.ChatRequest;
import com.groqai.model.ChatResponse;
import com.groqai.model.GroqApiModels;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroqServiceTest {

    @Mock private RestTemplate restTemplate;
    @Mock private GroqApiProperties props;
    @InjectMocks private GroqService groqService;

    @BeforeEach
    void setUp() {
        when(props.getModel()).thenReturn("llama3-8b-8192");
        when(props.getMaxTokens()).thenReturn(1024);
        when(props.getUrl()).thenReturn("https://api.groq.com/openai/v1/chat/completions");
        when(props.getKey()).thenReturn("test-key");
    }

    @Test
    void chat_success() {
        GroqApiModels.GroqMessage msg = new GroqApiModels.GroqMessage("assistant", "Hello from LLaMA!");
        GroqApiModels.Choice choice = new GroqApiModels.Choice();
        choice.setMessage(msg);
        choice.setFinishReason("stop");

        GroqApiModels.GroqResponseBody body = new GroqApiModels.GroqResponseBody();
        body.setId("chatcmpl-123");
        body.setModel("llama3-8b-8192");
        body.setChoices(Collections.singletonList(choice));

        GroqApiModels.GroqUsage usage = new GroqApiModels.GroqUsage();
        usage.setPromptTokens(10);
        usage.setCompletionTokens(15);
        usage.setTotalTokens(25);
        body.setUsage(usage);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(),
                eq(GroqApiModels.GroqResponseBody.class)))
                .thenReturn(ResponseEntity.ok(body));

        ChatResponse response = groqService.chat(new ChatRequest("Hello"));

        assertTrue(response.isSuccess());
        assertEquals("Hello from LLaMA!", response.getContent());
        assertEquals(25, response.getUsage().getTotalTokens());
    }

    @Test
    void chat_unauthorized_returnsError() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(),
                eq(GroqApiModels.GroqResponseBody.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));

        ChatResponse response = groqService.chat(new ChatRequest("Hello"));

        assertFalse(response.isSuccess());
        assertTrue(response.getErrorMessage().contains("401"));
    }

    @Test
    void chat_nullBody_returnsError() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(),
                eq(GroqApiModels.GroqResponseBody.class)))
                .thenReturn(ResponseEntity.ok(null));

        ChatResponse response = groqService.chat(new ChatRequest("Hello"));
        assertFalse(response.isSuccess());
    }
}
