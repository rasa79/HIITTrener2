package dev.radovanradivojevic.hiit2.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class TTSHelper(context: Context) {
    private var tts: TextToSpeech? = null
    private var isReady = false

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale("sr", "RS")
                isReady = true
            }
        }
    }

    fun speak(text: String) {
        if (isReady) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "HIITTTS")
        }
    }

    fun speakSprintStart(interval: Int) {
        speak("Sprint $interval")
    }

    fun speakRestStart() {
        speak("Odmor")
    }

    fun speakSpeedUp() {
        speak("Ubrzaj")
    }

    fun speakInZone() {
        speak("U zoni")
    }

    fun speakFinished() {
        speak("Kraj")
    }

    fun destroy() {
        tts?.stop()
        tts?.shutdown()
    }
}