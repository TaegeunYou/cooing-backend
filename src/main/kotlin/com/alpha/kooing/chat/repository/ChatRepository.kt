package com.alpha.kooing.chat.repository

import com.alpha.kooing.chat.entity.Chat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ChatRepository:JpaRepository<Chat,Long>{
    @Query("select c from Chat c where c.chatRoom.id=:roomId")
    fun findByChatRoomId(@Param("roomId") roomId:Long):List<Chat>

    @Query("select c from Chat c where c.unread>0 and c.user.id != :userId and c.chatRoom.id=:roomId order by c.createdAt")
    fun getUnreadChatByUserId(@Param("userId") userId:Long?, @Param("roomId") roomId:Long?):List<Chat>
}