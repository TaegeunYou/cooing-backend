package com.alpha.kooing.config

import com.alpha.kooing.user.Role
import com.alpha.kooing.user.User
import com.alpha.kooing.user.repository.UserRepository
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener

@Configuration
class InitData(
    val userRepository: UserRepository
){
    @EventListener(ApplicationReadyEvent::class)
    fun initData(){
        userRepository.save(User("kym8821","kym8821", Role.USER))
        userRepository.save(User("louie9798","louie9798", Role.USER))
    }
}