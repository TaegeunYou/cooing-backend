package com.alpha.kooing.chatRoom.repository

import com.alpha.kooing.chatRoom.entity.ChatRoom
import com.alpha.kooing.user.User
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository

@Repository
class CustomChatRoomRepositoryImpl(
    @PersistenceContext val entityManager: EntityManager
):CustomChatRoomRepository{
    override fun findByUserList(userList: List<User>): MutableList<*>? {
        val query = "SELECT c FROM ChatRoom c"
        val jpaQuery = entityManager.createQuery(query)
        val result = jpaQuery.resultList as MutableList<*>
        return result
    }
}