package com.mardillu.openai.model.action

import com.mardillu.openai.model.response.ModerationResponse

data class CGModeration(
    val input: String,
    val completionHandler: (ModerationResponse?, Throwable?) -> Unit
) : ChatGPTInputAction("text-moderation-latest", input)
