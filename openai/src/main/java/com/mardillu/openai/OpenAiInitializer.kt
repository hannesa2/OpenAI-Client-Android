package com.mardillu.openai


class OpenAiInitializer {
    companion object {
        internal var CHAT_GPT_API_KEY = ""
        fun initialize(apiKey: String) {
            CHAT_GPT_API_KEY = apiKey
        }
    }
}
