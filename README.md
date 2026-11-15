# Groq AI - Spring Boot Integration (100% FREE)

Spring Boot (Java 11) app using the **Groq FREE API** — no credit card, no expiry!

---

## ✅ How to Get FREE API Key (2 minutes)

1. Go to 👉 https://console.groq.com
2. Sign up with Google or email
3. Click **"API Keys"** → **"Create API Key"**
4. Copy your key (looks like: `gsk_xxxxxxxxxxxxxxxxxxxxxxxx`)

---

## ⚙️ Setup

Open `src/main/resources/application.properties` and paste your key:

```properties
groq.api.key=gsk_YOUR_KEY_HERE
```

---

## 🔨 Build

```bash
mvn clean package -DskipTests
```

JAR location: `target/groq-ai-springboot-1.0.0.jar`

---

## 🚀 Run

```bash
java -jar target/groq-ai-springboot-1.0.0.jar
```

Or pass key at runtime (more secure):
```bash
java -jar target/groq-ai-springboot-1.0.0.jar --groq.api.key=gsk_YOUR_KEY_HERE
```

App runs at: **http://localhost:8080**

---

## 🌐 API Endpoints

### POST /api/groq/chat — Full chat
```bash
curl -X POST http://localhost:8080/api/groq/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Explain Java 11 features",
    "systemPrompt": "You are a Java expert.",
    "model": "llama3-70b-8192",
    "maxTokens": 512
  }'
```

**Response:**
```json
{
  "id": "chatcmpl-abc123",
  "content": "Java 11 introduced several key features...",
  "model": "llama3-70b-8192",
  "finishReason": "stop",
  "usage": {
    "promptTokens": 20,
    "completionTokens": 150,
    "totalTokens": 170
  },
  "success": true
}
```

---

### GET /api/groq/ask?message=... — Quick question
```bash
curl "http://localhost:8080/api/groq/ask?message=What+is+Spring+Boot"
```

---

### GET /api/groq/models — List free models
```bash
curl http://localhost:8080/api/groq/models
```

---

### GET /api/groq/health — Health check
```bash
curl http://localhost:8080/api/groq/health
```

---

## 🆓 Free Models Available

| Model | Best For |
|---|---|
| `llama3-8b-8192` | Fast responses (default) |
| `llama3-70b-8192` | High quality answers |
| `mixtral-8x7b-32768` | Long documents & context |
| `gemma2-9b-it` | Google's Gemma model |

Change in `application.properties`:
```properties
groq.api.model=llama3-70b-8192
```

Or override per request by passing `"model"` in the request body.

---

## Project Structure

```
src/main/java/com/groqai/
├── GroqAiApplication.java
├── config/
│   ├── AppConfig.java           ← RestTemplate bean
│   └── GroqApiProperties.java   ← Config binding
├── controller/
│   ├── GroqController.java      ← REST endpoints
│   └── GlobalExceptionHandler.java
├── model/
│   ├── ChatRequest.java
│   ├── ChatResponse.java
│   └── GroqApiModels.java       ← Groq API JSON models
└── service/
    └── GroqService.java         ← Business logic
```
