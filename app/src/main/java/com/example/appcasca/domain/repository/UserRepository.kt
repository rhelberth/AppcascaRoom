package com.example.appcasca.domain.repository

import com.example.appcasca.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsers(): Flow<List<User>>
    suspend fun addUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun deleteUser(user: User)
}