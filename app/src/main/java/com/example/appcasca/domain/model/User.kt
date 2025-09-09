package com.example.appcasca.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users") // Opcional: define o nome da tabela
data class User(
    @PrimaryKey val id: Int,
    val name: String,
    val email: String
)