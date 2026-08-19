package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// --- Gemini API Models ---

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    val contents: List<GeminiContent>,
    val generationConfig: GeminiGenerationConfig? = null,
    val tools: List<Map<String, Any>>? = null,
    val systemInstruction: GeminiContent? = null
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
    val role: String? = null,
    val parts: List<GeminiPart>
)

@JsonClass(generateAdapter = true)
data class GeminiPart(
    val text: String? = null,
    val inlineData: GeminiInlineData? = null
)

@JsonClass(generateAdapter = true)
data class GeminiInlineData(
    val mimeType: String,
    val data: String
)

@JsonClass(generateAdapter = true)
data class GeminiGenerationConfig(
    val temperature: Float? = null,
    val topP: Float? = null,
    val topK: Int? = null,
    val thinkingConfig: GeminiThinkingConfig? = null,
    val imageConfig: GeminiImageConfig? = null,
    val responseModalities: List<String>? = null,
    val speechConfig: GeminiSpeechConfig? = null
)

@JsonClass(generateAdapter = true)
data class GeminiThinkingConfig(
    val thinkingLevel: String
)

@JsonClass(generateAdapter = true)
data class GeminiImageConfig(
    val aspectRatio: String = "1:1",
    val imageSize: String = "1K"
)

@JsonClass(generateAdapter = true)
data class GeminiSpeechConfig(
    val voiceConfig: GeminiVoiceConfig
)

@JsonClass(generateAdapter = true)
data class GeminiVoiceConfig(
    val prebuiltVoiceConfig: GeminiPrebuiltVoiceConfig
)

@JsonClass(generateAdapter = true)
data class GeminiPrebuiltVoiceConfig(
    val voiceName: String = "Kore"
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    val candidates: List<GeminiCandidate>? = null,
    val promptFeedback: Any? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
    val content: GeminiContent? = null,
    val finishReason: String? = null
)

// --- Exa Search Engine Models ---

@JsonClass(generateAdapter = true)
data class ExaSearchRequest(
    val query: String,
    val numResults: Int = 5,
    val useAutoprompt: Boolean = true,
    val type: String = "neural",
    val contents: ExaContentOptions? = ExaContentOptions(text = true)
)

@JsonClass(generateAdapter = true)
data class ExaContentOptions(
    val text: Boolean = true
)

@JsonClass(generateAdapter = true)
data class ExaSearchResponse(
    val results: List<ExaResult>? = null,
    val autopromptString: String? = null
)

@JsonClass(generateAdapter = true)
data class ExaResult(
    val title: String? = null,
    val url: String? = null,
    val id: String? = null,
    val score: Float? = null,
    val publishedDate: String? = null,
    val author: String? = null,
    val text: String? = null,
    val highlights: List<String>? = null
)

// --- Grok / xAI Models ---

@JsonClass(generateAdapter = true)
data class GrokChatRequest(
    val messages: List<GrokMessage>,
    val model: String = "grok-2-latest",
    val stream: Boolean = false,
    val temperature: Float = 0.7f
)

@JsonClass(generateAdapter = true)
data class GrokMessage(
    val role: String,
    val content: String
)

@JsonClass(generateAdapter = true)
data class GrokChatResponse(
    val choices: List<GrokChoice>? = null
)

@JsonClass(generateAdapter = true)
data class GrokChoice(
    val message: GrokMessage? = null
)
