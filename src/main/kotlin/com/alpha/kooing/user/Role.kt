package com.alpha.kooing.user

enum class Role(key: String, role: String) {
    // 로그인하지 않은 사용자
    GUEST("ROLE_GUSET", "guest"),
    // 회원가입하지 않았지만, 구글 소셜 인증은 성공한 사용자
    LIMITED("ROLE_LIMITED", "limited"),
    // 로그인했고 구글 소셜 인증까지 성공한 사용자
    USER("ROLE_USER", "user"),
    // 관리자
    ADMIN("ROLE_ADMIN", "admin")
}