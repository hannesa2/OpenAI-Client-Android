package com.mardillu.openai

import android.app.Application
import com.mardillu.openai.test.R

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        OpenAiInitializer.initialize(getString(R.string.OPEN_AI_API_KEY))
    }
}