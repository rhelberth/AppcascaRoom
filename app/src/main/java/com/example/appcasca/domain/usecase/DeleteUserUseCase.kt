package com.example.appcasca.domain.usecase

import com.example.appcasca.domain.model.User
import com.example.appcasca.domain.repository.UserRepository

class DeleteUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(user: User) {
        userRepository.deleteUser(user)
    }
}