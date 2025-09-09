package com.example.appcasca.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appcasca.data.local.dao.UserDao
import com.example.appcasca.domain.model.User

@Database(entities = [User::class], version = 1, exportSchema = false) // exportSchema = false para simplificar, mas considere true para projetos reais
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}