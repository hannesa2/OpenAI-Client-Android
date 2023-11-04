package com.mardillu.openai.model.action

import com.mardillu.openai.model.response.CreateImageResponse

data class CG_CreateImage(
    val promptVal: String,
    val n: Int = 2,
    val size: String = "1024x1024",
    val completionHandler: (CreateImageResponse?, Throwable?) -> Unit
) : ChatGPTInputAction("", promptVal)
