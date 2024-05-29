package com.alpha.kooing.user

import com.alpha.kooing.board.entity.*
import com.alpha.kooing.chat.entity.Chat
import com.alpha.kooing.chatMatching.entity.ChatMatching
import com.alpha.kooing.notification.entity.Notification
import com.alpha.kooing.reward.entity.UserReward
import com.alpha.kooing.support.entity.JobPostingScrap
import com.alpha.kooing.support.entity.SupportBusinessScrap
import com.alpha.kooing.support.entity.SupportPolicyScrap
import com.alpha.kooing.user.dto.UserDetail
import com.alpha.kooing.user.dto.UserResponseDto
import com.alpha.kooing.user.entity.UserConcernKeyword
import com.alpha.kooing.user.entity.UserInterestKeyword
import com.alpha.kooing.user.enum.RoleType
import jakarta.persistence.*
import java.time.LocalDateTime

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
    var chats: MutableList<Chat> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val boards: MutableList<Board> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val comments: MutableList<Comment> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val scraps: MutableList<Scrap> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val likes: MutableList<Likes> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val clubs: MutableList<Club> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val studies: MutableList<Study> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val volunteers: MutableList<Volunteer> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val userInterestKeyword: MutableList<UserInterestKeyword> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val userConcernKeyword: MutableList<UserConcernKeyword> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val notifications: MutableList<Notification> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val userRewards: MutableList<UserReward> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val supportPolicyScraps: MutableList<SupportPolicyScrap> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val supportBusinessScraps: MutableList<SupportBusinessScrap> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val jobPostingScraps: MutableList<JobPostingScrap> = mutableListOf(),

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    val roleType: RoleType,

    @Column(nullable = false)
    var profileMessage: String,

    @Column(nullable = true)
    var profileImageUrl: String?,

    @Column(nullable = false)
    var isMatchingActive: Boolean,

    @Column(nullable = false)
    var attendanceCount: Int = 0,

    @Column(nullable = false)
    var lastLoginDatetime: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var house: String = "",

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long? = null
){
    fun toResponseDto():UserResponseDto{
        return UserResponseDto(
            id=this.id,
            email = this.email,
            username = this.username,
            role = this.role,
            profileMessage = this.profileMessage,
            profileImageUrl = this.profileImageUrl,
            userConcernKeyword = this.userConcernKeyword.map { it.concernKeyword.name },
            userInterestKeyword = this.userInterestKeyword.map { it.interestKeyword.name }
        )
    }

    fun toUserDetail():UserDetail{
        return UserDetail(
            name = this.username,
            role = this.roleType,
            profileMessage = this.profileMessage,
            profileImageUrl = this.profileImageUrl,
            interestKeyword = this.userInterestKeyword.map { it.interestKeyword.name },
            concernKeyword = this.userConcernKeyword.map { it.concernKeyword.name },
            rewards = this.userRewards.groupBy {
                it.reward.rewardType
            }.map {
                UserDetail.RewardDetail(
                    it.key.name,
                    it.value.size
                )
            },
            this.isMatchingActive
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

    fun attend() {
        this.attendanceCount++
    }

    fun updateHouse(houseStringFormat: String) {
        this.house = houseStringFormat
    }

}