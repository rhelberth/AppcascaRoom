package com.example.appcasca.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.appcasca.domain.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User) // Adicionado para inserir um único usuário

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<User>)

    @Update
    suspend fun update(user: User) // Adicionado para atualizar um usuário

    @Delete
    suspend fun delete(user: User) // Adicionado para deletar um usuário

    @Query("SELECT * FROM users ORDER BY id ASC") // Adicionado ORDER BY para consistência
    fun getAll(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: Int): Flow<User?>

    @Query("DELETE FROM users")
    suspend fun deleteAll()
}