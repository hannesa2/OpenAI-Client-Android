package com.mardillu.openai

import com.mardillu.openai.test.R
import info.hannes.logcat.LoggingApplication

class App : LoggingApplication() {
    override fun onCreate() {
        super.onCreate()
        OpenAiInitializer.initialize(getString(R.string.OPEN_AI_API_KEY))
    }
}