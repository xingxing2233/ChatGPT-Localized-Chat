package com.example.xingxingyukejigpt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast
import android.widget.TextView
import com.example.xingxingyukejigpt.R
import com.example.xingxingyukejigpt.HttpUtil
import com.example.xingxingyukejigpt.ChatResponse
import android.util.Log
import android.os.Handler

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sendButton.setOnClickListener {
            val message = messageEditText.text.toString()
            if (message.isNotBlank()) {
                HttpUtil.sendMessage(message, object : Callback<ChatResponse> {
                    override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                        if (response.isSuccessful) {
                            val chatResponse = response.body()
                            val modelResponse = chatResponse?.message
                            val answerTextView = findViewById<TextView>(R.id.answerTextView)

                            val handler = Handler()
                            val delayMillis = 150 // 每个字之间的时间间隔（毫秒）

                            var currentIndex = 0


                            if (modelResponse != null && (modelResponse.startsWith("?") ||
                                        modelResponse.startsWith("，") ||
                                        modelResponse.startsWith("。") ||
                                        modelResponse.startsWith("！") ||
                                        modelResponse.startsWith("\\/\\/")
                                        )) {
                                // 去掉开头的符号
                                val filteredResponse = modelResponse.substring(1)

                                // 更新 modelResponse，使其为去掉开头符号后的字符串
                                val modelResponse = filteredResponse

                                // 更新 UI 显示服务器返回的消息（去掉符号后的消息）
                                val answerTextView = findViewById<TextView>(R.id.answerTextView)

                                val runnable = object : Runnable {
                                    override fun run() {
                                        if (currentIndex < modelResponse.length) {
                                            val currentChar = modelResponse[currentIndex]
                                            answerTextView.append(currentChar.toString()) // 逐字追加到TextView
                                            currentIndex++
                                            handler.postDelayed(this, delayMillis.toLong()) // 延迟指定的时间后再执行下一个字
                                        }
                                    }
                                }

                                handler.post(runnable) // 启动逐字显示的Runnable
                            } else {
                                // 模型的回答不以常见符号开头，直接显示在 UI 中
                                val answerTextView = findViewById<TextView>(R.id.answerTextView)
                                answerTextView.text = modelResponse
                            }




                            println("服务器返回的消息是：$message") // 输出：服务器返回的消息是：Hello, World!

                            // 处理服务器返回的文本
                            // 在这里你可以更新 UI 显示服务器返回的消息
                            // 将模型的回答显示在回答框中

                        }
                    }

                    override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                        Log.e("NetworkError", "Error: ${t.message}")

                        t.printStackTrace()
                        // 处理网络请求失败的情况
                        Toast.makeText(this@MainActivity, "网络请求失败，请检查网络连接", Toast.LENGTH_SHORT).show()

                    }
                })

                // 清空输入框的内容
                messageEditText.text.clear()
            }
        }
    }
}
