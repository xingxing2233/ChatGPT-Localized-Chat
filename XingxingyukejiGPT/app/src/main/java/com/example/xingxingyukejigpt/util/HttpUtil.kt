package com.example.xingxingyukejigpt

import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
object HttpUtil {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://0.0.0.0:5000") // 服务器地址
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val chatApi = retrofit.create(ChatApi::class.java)

    fun sendMessage(message: String, callback: Callback<ChatResponse>) {
        val request = ChatRequest(message)
        val call = chatApi.sendMessage(request)
        call.enqueue(callback)
    }
}