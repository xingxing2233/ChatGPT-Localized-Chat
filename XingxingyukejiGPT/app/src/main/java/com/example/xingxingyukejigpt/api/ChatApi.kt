package com.example.xingxingyukejigpt


import retrofit2.http.GET

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class ChatRequest(val message: String)
data class ChatResponse(val message: String)

interface ChatApi {
    @POST("chat")
    fun sendMessage(@Body request: ChatRequest): Call<ChatResponse>
}


