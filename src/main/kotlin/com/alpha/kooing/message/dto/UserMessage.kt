package com.alpha.kooing.message.dto

import java.io.Serializable

class UserMessage(
    var content:String,
    val senderId:Long,
    val roomId:Long?,
):Serializable