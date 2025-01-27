package com.mardillu.openai.model.action

import com.mardillu.openai.model.response.SimpleTextResponse
import java.io.File

data class CGCreateTranslation(
    val file: File,
    val completionHandler: (SimpleTextResponse?, Throwable?) -> Unit
) : ChatGPTInputAction("whisper-1", file.name)
