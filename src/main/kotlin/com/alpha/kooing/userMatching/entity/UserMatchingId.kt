package com.alpha.kooing.userMatching.entity

import com.alpha.kooing.matching.entity.Matching
import com.alpha.kooing.user.entity.User
import java.io.Serializable

class UserMatchingId(
    var user: User,
    var matching: Matching
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as UserMatchingId

        if (user != other.user) return false
        if (matching != other.matching) return false

        return true
    }

    override fun hashCode(): Int {
        var result = user.hashCode()
        result = 31 * result + matching.hashCode()
        return result
    }
}
