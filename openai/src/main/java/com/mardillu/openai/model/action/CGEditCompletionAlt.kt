package com.mardillu.openai.model.action

import com.mardillu.openai.model.response.TextCompletionResponse

data class CGEditCompletionAlt(
    val input: String,
    val instruction: String,
    val completionHandler: (TextCompletionResponse?, Throwable?) -> Unit
) : ChatGPTInputAction("text-davinci-003", input)
