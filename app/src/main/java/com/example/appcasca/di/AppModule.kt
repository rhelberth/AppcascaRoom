package com.example.appcasca.di

import android.content.Context
import androidx.room.Room
import com.example.appcasca.data.local.AppDatabase
import com.example.appcasca.data.local.dao.UserDao
import com.example.appcasca.data.repository.UserRepositoryImpl
import com.example.appcasca.domain.repository.UserRepository
import com.example.appcasca.domain.usecase.AddUserUseCase
import com.example.appcasca.domain.usecase.DeleteUserUseCase
import com.example.appcasca.domain.usecase.GetUsersUseCase
import com.example.appcasca.domain.usecase.UpdateUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val DATABASE_NAME = "appcasca_database"

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration(false)
        .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepositoryImpl(userDao)
    }

    @Provides
    @Singleton
    fun provideGetUsersUseCase(userRepository: UserRepository): GetUsersUseCase {
        return GetUsersUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideAddUserUseCase(userRepository: UserRepository): AddUserUseCase {
        return AddUserUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateUserUseCase(userRepository: UserRepository): UpdateUserUseCase {
        return UpdateUserUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteUserUseCase(userRepository: UserRepository): DeleteUserUseCase {
        return DeleteUserUseCase(userRepository)
    }
}