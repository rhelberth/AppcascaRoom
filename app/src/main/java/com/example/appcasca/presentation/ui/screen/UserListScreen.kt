package com.example.appcasca.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.appcasca.domain.model.User
import com.example.appcasca.presentation.viewmodel.UserListUiState
import com.example.appcasca.presentation.viewmodel.UserViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlin.random.Random // Corrigido o import

@Composable
fun UserListScreen(viewModel: UserViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.userMessage.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }, // Adicionado SnackbarHost
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val nextId = ( (uiState as? UserListUiState.Success)?.users?.maxOfOrNull { it.id } ?: 0) + 1
                val newUser = User(
                    // Gera um ID incremental simples ou usa um aleatório maior para evitar colisões com IDs iniciais
                    id = if ((uiState as? UserListUiState.Success)?.users?.isEmpty() == true) 1 else nextId,
                    name = "New User ${Random.nextInt(100)}",
                    email = "new.user${Random.nextInt(100)}@example.com"
                )
                viewModel.addUser(newUser)
            }) {
                Icon(Icons.Filled.Add, "Add User")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is UserListUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is UserListUiState.Success -> {
                    if (state.users.isEmpty()) {
                        Text("No users found. Add some!")
                    } else {
                        UserList(users = state.users, viewModel = viewModel)
                    }
                }
                is UserListUiState.Error -> {
                    // Este Text é para o erro ao carregar a lista. Erros de CRUD são via Snackbar.
                    Text(text = "Error loading list: ${state.message}", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun UserList(users: List<User>, viewModel: UserViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp) // Ajustado padding
    ) {
        items(users, key = { user -> user.id }) { user ->
            UserItem(user = user, viewModel = viewModel)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun UserItem(user: User, viewModel: UserViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
            Text(text = user.name, style = MaterialTheme.typography.titleMedium)
            Text(text = user.email, style = MaterialTheme.typography.bodySmall)
        }
        Row {
            IconButton(onClick = {
                val updatedUser = user.copy(name = user.name + " (Edited)")
                viewModel.updateUser(updatedUser)
            }) {
                Icon(Icons.Filled.Edit, contentDescription = "Edit User")
            }
            IconButton(onClick = { viewModel.deleteUser(user) }) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete User")
            }
        }
    }
}
