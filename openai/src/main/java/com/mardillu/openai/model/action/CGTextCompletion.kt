package com.mardillu.openai.model.action

import com.mardillu.openai.model.response.TextCompletionResponse

data class CGTextCompletion(
    val promptVal: String,
    val maxTokens: Int = 16,
    val temperature: Double = 1.0,
    val top_p: Double = 1.0,
    val stream: Boolean = false,
    val logprobs: Int? = null,
    val stop: String? = null,
    val completionHandler: (TextCompletionResponse?, Throwable?) -> Unit
) : ChatGPTInputAction("text-davinci-003", prompt = promptVal)
