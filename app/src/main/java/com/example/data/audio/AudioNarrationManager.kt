package com.example.data.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class AudioNarrationManager(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentUtterance = MutableStateFlow("")
    val currentUtterance: StateFlow<String> = _currentUtterance.asStateFlow()

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (e: Exception) {
            Log.e("AudioNarrationManager", "Failed to init TTS: ${e.message}")
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.let { engine ->
                val result = engine.setLanguage(Locale.US)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    engine.setLanguage(Locale.getDefault())
                }
                engine.setPitch(1.05f)
                engine.setSpeechRate(1.0f)
                engine.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        _isPlaying.value = true
                    }

                    override fun onDone(utteranceId: String?) {
                        _isPlaying.value = false
                        _currentUtterance.value = ""
                    }

                    override fun onError(utteranceId: String?) {
                        _isPlaying.value = false
                        _currentUtterance.value = ""
                    }
                })
                isInitialized = true
            }
        }
    }

    fun speak(text: String, voiceName: String = "Grok-CyberVoice") {
        if (text.isBlank()) return
        _currentUtterance.value = text.take(120)
        if (isInitialized && tts != null) {
            val cleanText = text.replace(Regex("[#*`_{}\\[\\]]"), " ").take(1000)
            tts?.speak(cleanText, TextToSpeech.QUEUE_FLUSH, null, "UTTERANCE_${System.currentTimeMillis()}")
        } else {
            _isPlaying.value = true
            // Mock pulse for UI feedback if TTS is initializing
        }
    }

    fun stop() {
        if (isInitialized) {
            tts?.stop()
        }
        _isPlaying.value = false
        _currentUtterance.value = ""
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
