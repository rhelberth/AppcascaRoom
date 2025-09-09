package com.example.appcasca.domain.usecase

import com.example.appcasca.domain.model.User
import com.example.appcasca.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUsersUseCase(private val userRepository: UserRepository) {
    operator fun invoke(): Flow<List<User>> {
        return userRepository.getUsers()
    }
}