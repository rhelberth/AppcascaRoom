package com.example.appcasca.domain.usecase

import com.example.appcasca.domain.model.User
import com.example.appcasca.domain.repository.UserRepository

class AddUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(user: User) {
        userRepository.addUser(user)
    }
}