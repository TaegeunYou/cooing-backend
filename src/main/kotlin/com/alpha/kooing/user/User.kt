package com.alpha.kooing.user

import com.alpha.kooing.board.entity.*
import com.alpha.kooing.chatMatching.entity.ChatMatching
import com.alpha.kooing.user.dto.UserDetail
import com.alpha.kooing.user.dto.UserResponseDto
import com.alpha.kooing.user.entity.UserConcernKeyword
import com.alpha.kooing.user.entity.UserInterestKeyword
import com.alpha.kooing.user.enum.RoleType
import jakarta.persistence.*

@Entity
class User(
    @Column(name = "email", nullable = false)
    val email:String,

    @Column(name = "name", nullable = false)
    var username:String,

    @Column(name = "role", nullable = false)
    val role: Role,

    @OneToMany
    @JoinColumn(name = "user_id")
    var chatMatching: List<ChatMatching> = listOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val boards: MutableList<Board> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val comments: MutableList<Comment> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val scraps: MutableList<Scrap> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val clubs: MutableList<Club> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val studies: MutableList<Study> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val userInterestKeyword: MutableList<UserInterestKeyword> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val userConcernKeyword: MutableList<UserConcernKeyword> = mutableListOf(),

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    val roleType: RoleType,

    @Column(nullable = false)
    var profileMessage: String,

    @Column(nullable = true)
    var profileImageUrl: String?,

    @Column(nullable = false)
    var isMatchingActive: Boolean,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long? = null
){
    fun toResponseDto():UserResponseDto{
        return UserResponseDto(
            id=this.id,
            email = this.email,
            username = this.username,
            role = this.role
        )
    }

    fun toUserDetail():UserDetail{
        return UserDetail(
            name = this.username,
            role = this.roleType,
            profileMessage = this.profileMessage,
            profileImageUrl = this.profileImageUrl,
            interestKeyword = this.userInterestKeyword.map { it.interestKeyword.name },
            concernKeyword = this.userConcernKeyword.map { it.concernKeyword.name }
        )
    }

    fun updateProfile(name: String, profileMessage: String) {
        this.username = name
        this.profileMessage = profileMessage
    }

    fun updateProfileImage(url: String) {
        this.profileImageUrl = url
    }

    fun updateMatchingStatus(isMatchingActive: Boolean) {
        this.isMatchingActive = isMatchingActive
    }

    override fun toString(): String {
        return "User(email='$email', username='$username', role=$role)"
    }

}