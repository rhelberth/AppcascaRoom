package com.example.appcasca.data.repository

import com.example.appcasca.data.local.dao.UserDao
import com.example.appcasca.domain.model.User
import com.example.appcasca.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override fun getUsers(): Flow<List<User>> {
        return userDao.getAll()
    }

    override suspend fun addUser(user: User) {
        userDao.insert(user)
    }

    override suspend fun updateUser(user: User) {
        userDao.update(user)
    }

    override suspend fun deleteUser(user: User) {
        userDao.delete(user)
    }
}