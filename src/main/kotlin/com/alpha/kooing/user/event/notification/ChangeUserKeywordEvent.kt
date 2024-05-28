package com.alpha.kooing.user.event.notification

import com.alpha.kooing.user.User
import com.alpha.kooing.user.enum.RoleType

class ChangeUserKeywordEvent(
    val user: User,
    val mateRoleType: RoleType
)