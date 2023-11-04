package com.mardillu.openai.model.action

import com.mardillu.openai.model.response.CreateEmbeddingResponse

data class CGEmbeddings(
    val input: String,
    val completionHandler: (CreateEmbeddingResponse?, Throwable?) -> Unit
) : ChatGPTInputAction("text-embedding-ada-002", input)
