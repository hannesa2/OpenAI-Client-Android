package com.mardillu.openai.model.action

import com.mardillu.openai.model.Message
import com.mardillu.openai.model.response.ChatCompletionResponse

data class CG_ChatCompletion(
    val messages: List<Message>,
    val completionHandler: (ChatCompletionResponse?, Throwable?) -> Unit
) : ChatGPTInputAction("gpt-3.5-turbo", messages.get(0).content)
