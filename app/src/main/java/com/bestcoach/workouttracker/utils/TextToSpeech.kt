package com.bestcoach.workouttracker.utils

import android.app.Activity
import android.speech.tts.TextToSpeech
import android.widget.Toast
import java.util.*
/**
 * class for converting string to speech.
 */
class TextToSpeech(private val activity: Activity) : TextToSpeech.OnInitListener {

    private val tts: TextToSpeech = TextToSpeech(activity, this)
    // fun to initialize TTS and check if it is open correctly
    override fun onInit(i: Int) {
        if (i == TextToSpeech.SUCCESS) {

            val result: Int = tts.setLanguage(Locale.getDefault())

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(activity, "This Language is not supported", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(activity, "Initialization Failed!", Toast.LENGTH_SHORT).show()
        }
    }
// fun to convert String To oSpeech
    fun speakOut(message: String) {
        tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
    }
// fun to shutdown TextToSpeech from running
    fun stopTTS() {
        tts.stop()
        tts.shutdown()
    }
// fun  check if TTS is finish or not
    fun isTTSSpeaking(): Boolean {
        return tts.isSpeaking
    }
}