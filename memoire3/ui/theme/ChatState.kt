package com.example.memoire3.ui.theme

import android.graphics.Bitmap
import com.example.memoire3.data.Chat


data class ChatState (
    val chatList: MutableList<Chat> = mutableListOf(),
    val prompt: String = "",
    val bitmap: Bitmap? = null
)