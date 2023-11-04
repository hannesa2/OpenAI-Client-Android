package com.mardillu.openai.model.action

import com.mardillu.openai.model.response.CreateImageResponse
import java.io.File

data class CGCreateImageEdit(
    val image: File,
    val promptVal: String,
    val mask: File? = null,
    val n: Int = 1,
    val size: String = "1024x1024",
    val completionHandler: (CreateImageResponse?, Throwable?) -> Unit
) : ChatGPTInputAction("", promptVal)
