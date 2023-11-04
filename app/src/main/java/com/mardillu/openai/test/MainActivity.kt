package com.mardillu.openai.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mardillu.openai.model.action.CGChatCompletion
import com.mardillu.openai.model.action.CGCreateImage
import com.mardillu.openai.model.action.CGCreateImageEdit
import com.mardillu.openai.model.action.CGCreateImageVariation
import com.mardillu.openai.model.action.CGCreateTranscription
import com.mardillu.openai.model.action.CGCreateTranslation
import com.mardillu.openai.model.action.CGEditCompletionAlt
import com.mardillu.openai.model.action.CGEmbeddings
import com.mardillu.openai.model.action.CGModeration
import com.mardillu.openai.model.action.CGTextCompletion
import com.mardillu.openai.model.action.ChatGPTInputAction
import com.mardillu.openai.model.Message
import com.mardillu.openai.network.LoggingClient
import com.mardillu.openai.network.OpenApiClient
import com.mardillu.openai.test.databinding.ActivityMainBinding
import com.mardillu.openai.message.MessageAdapter
import com.mardillu.openai.message.TrayMessage
import com.mardillu.openai.message.TrayState
import com.mardillu.openai.message.TrayType
import java.io.File
import java.util.LinkedList

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val chatGPTInputActions = LinkedList<ChatGPTInputAction>()
    private lateinit var chatGptService: OpenApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatGptService = OpenApiClient()
        val loggingApiService = LoggingClient()

//        loggingApiService.logRequestTime(
//            "test/test",
//            200,
//            500,
//            300,
//            200,
//        ){ result, error ->
//            if (error != null){
//                Log.d("TAG", "onCreate: =======> FAILED <============")
//                error.printStackTrace()
//            } else {
//                Log.d("TAG", "onCreate: =======> SUCCESS <============")
//            }
//        }

        chatGPTInputActions.add(CGTextCompletion("Hello chat gpt! what is the meaning of life?") { result, error ->
            if (error != null) {
                // Handle error
                trayState.addMessage(TrayMessage(TrayType.ERROR, error.toString()))
            } else if (result != null) {
            }
            takeNextAction(result?.choices?.get(0)?.text)
        })
        chatGPTInputActions.add(
            CGChatCompletion(
                messages = listOf(Message("user", "What is the update with your weekly PR review"))
            ) { result, error ->
                if (error != null) {
                    // Handle error
                    trayState.addMessage(TrayMessage(TrayType.ERROR, error.toString()))
                } else if (result != null) {

                }
                takeNextAction(result?.choices?.get(0)?.message?.content)
            })
        chatGPTInputActions.add(
            CGEditCompletionAlt(
                input = "What day of the wek is it?",
                instruction = "Fix the spelling mistakes"
            ) { result, error ->
                if (error != null) {
                    // Handle error
                    trayState.addMessage(TrayMessage(TrayType.ERROR, error.toString()))
                } else if (result != null) {

                }
                takeNextAction(result?.choices?.get(0)?.text)
            })
        chatGPTInputActions.add(CGEmbeddings("Hello chat gpt! what is the meaning of life?") { result, error ->
            if (error != null) {
                // Handle error
                trayState.addMessage(TrayMessage(TrayType.ERROR, error.toString()))
            } else if (result != null) {
            }
            takeNextAction(result?.data?.get(0)?.embedding?.size.toString())
        })
        chatGPTInputActions.add(CGCreateImage("A cute baby sea otter") { result, error ->
            if (error != null) {
                // Handle error
                trayState.addMessage(TrayMessage(TrayType.ERROR, error.toString()))
            } else if (result != null) {
            }
            takeNextAction(null, result?.data?.get(0)?.url)
        })
        chatGPTInputActions.add(CGModeration("I want to kill them.") { result, error ->
            if (error != null) {
                // Handle error
                trayState.addMessage(TrayMessage(TrayType.ERROR, error.toString()))
            } else if (result != null) {
            }
            takeNextAction(result?.results?.get(0)?.categories?.hate.toString())
        })
        chatGPTInputActions.add(CGCreateImageEdit(
            imageFromAssets("img.png"),
            "A cute cat sitting on a white table",
            imageFromAssets("img.png")
        ) { result, error ->
            if (error != null) {
                // Handle error
                trayState.addMessage(TrayMessage(TrayType.ERROR, error.toString()))
            } else if (result != null) {
            }
            takeNextAction(null, result?.data?.get(0)?.url)
        })
        chatGPTInputActions.add(CGCreateImageVariation(imageFromAssets("img.png")) { result, error ->
            if (error != null) {
                // Handle error
                trayState.addMessage(TrayMessage(TrayType.ERROR, error.toString()))
            } else if (result != null) {
            }
            takeNextAction(null, result?.data?.get(0)?.url)
        })
        chatGPTInputActions.add(CGCreateTranscription(imageFromAssets("audio.m4a")) { result, error ->
            if (error != null) {
                // Handle error
                trayState.addMessage(TrayMessage(TrayType.ERROR, result?.text ?: error.toString()))
            } else if (result != null) {
            }
            takeNextAction(result?.text)
        })
        chatGPTInputActions.add(CGCreateTranslation(imageFromAssets("audio.m4a")) { result, error ->
            if (error != null) {
                // Handle error
                trayState.addMessage(TrayMessage(TrayType.ERROR, result?.text ?: error.toString()))
            } else if (result != null) {
            }
            takeNextAction(result?.text)
        })

        configureMessageStream()
        takeNextAction(null)
    }

    private fun takeNextAction(previousOutput: String?, url: String? = null) {
        url?.let {
            trayState.addMessage(TrayMessage(TrayType.AI, content = "", imageURL = it))
        } ?: run {
            previousOutput?.let {
                trayState.addMessage(TrayMessage(TrayType.AI, content = it))
            }
        }
        chatGPTInputActions.poll()?.let {
            trayState.addMessage(TrayMessage(TrayType.USER, it.prompt))
            chatGptService.runAction(it)
        }
    }

    private fun imageFromAssets(name: String): File {
        val inputStream = applicationContext.assets.open(name)
        val suf = if (name.contains("m4a")) "suf.m4a" else "suf"
        val file = File.createTempFile("pre", suf)
        file.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
        inputStream.close()

        return file
    }

    private fun configureMessageStream() {
        val viewAdapter = MessageAdapter(this)
        viewAdapter.submitList(trayState.messages)

        val observer = { messages: List<TrayMessage> ->
            val lastMessage = messages.size - 1
            viewAdapter.notifyItemChanged(lastMessage)
            binding.messageStream.scrollToPosition(lastMessage)
        }
        viewAdapter.onImageLoad = observer

        // observe messages for changes
        trayState.liveData().observe(this, observer)

        binding.messageStream.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = viewAdapter
        }
    }

    companion object {
        internal val trayState: TrayState = TrayState(messages = arrayListOf())
    }
}
