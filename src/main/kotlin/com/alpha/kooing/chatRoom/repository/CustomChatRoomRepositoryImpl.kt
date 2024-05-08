package com.alpha.kooing.chatRoom.repository

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository

@Repository
class CustomChatRoomRepositoryImpl(
    @PersistenceContext val entityManager: EntityManager
):CustomChatRoomRepository{
    override fun findByUserList(userIdList: List<String>): MutableList<*>? {
        val userCount = userIdList.size
        val query = """
            SELECT cr FROM ChatRoom cr WHERE cr.id IN (
               SELECT cm.chatRoom.id FROM ChatMatching cm
               WHERE cm.user.id IN :userIdList
               GROUP BY cm.chatRoom.id
               HAVING COUNT(DISTINCT cm.user.id) = :userCount
            )
        """.trimIndent()

        val jpaQuery = entityManager.createQuery(query)
        jpaQuery.setParameter("userIdList", userIdList)
        jpaQuery.setParameter("userCount", userCount)
        val result = jpaQuery.resultList as MutableList<*>
        return result
    }
}