package com.example.appcasca.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcasca.domain.model.User
import com.example.appcasca.domain.usecase.AddUserUseCase
import com.example.appcasca.domain.usecase.DeleteUserUseCase
import com.example.appcasca.domain.usecase.GetUsersUseCase
import com.example.appcasca.domain.usecase.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

// Estado da UI para a lista de usuários
sealed class UserListUiState {
    object Loading : UserListUiState()
    data class Success(val users: List<User>) : UserListUiState()
    data class Error(val message: String) : UserListUiState() // Este Error será para falhas ao carregar a lista inicial
}

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val addUserUseCase: AddUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserListUiState>(UserListUiState.Loading)
    val uiState: StateFlow<UserListUiState> = _uiState.asStateFlow()

    private val _userMessage = MutableSharedFlow<String>()
    val userMessage: SharedFlow<String> = _userMessage.asSharedFlow()

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            // _uiState.value = UserListUiState.Loading // Já definido no init do _uiState
            getUsersUseCase()
                .catch { exception ->
                    _uiState.value = UserListUiState.Error("Failed to load users: ${exception.message ?: "Unknown error"}")
                }
                .collect { users ->
                    _uiState.value = UserListUiState.Success(users)
                }
        }
    }

    fun addUser(user: User) {
        viewModelScope.launch {
            try {
                addUserUseCase(user)
                _userMessage.emit("User added successfully")
            } catch (e: Exception) {
                _userMessage.emit("Error adding user: ${e.message ?: "Unknown error"}")
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            try {
                updateUserUseCase(user)
                _userMessage.emit("User updated successfully")
            } catch (e: Exception) {
                _userMessage.emit("Error updating user: ${e.message ?: "Unknown error"}")
            }
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            try {
                deleteUserUseCase(user)
                _userMessage.emit("User deleted successfully")
            } catch (e: Exception) {
                _userMessage.emit("Error deleting user: ${e.message ?: "Unknown error"}")
            }
        }
    }
}