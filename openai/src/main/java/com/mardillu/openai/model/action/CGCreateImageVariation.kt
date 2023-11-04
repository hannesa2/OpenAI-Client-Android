package com.mardillu.openai.model.action

import com.mardillu.openai.model.response.CreateImageResponse
import java.io.File

data class CGCreateImageVariation(
    val image: File,
    val n: Int = 1,
    val size: String = "1024x1024",
    val completionHandler: (CreateImageResponse?, Throwable?) -> Unit
) : ChatGPTInputAction("", image.name)
