package br.com.cinequiz.ui

import androidx.lifecycle.*
import br.com.cinequiz.domain.Usuario
import br.com.cinequiz.room.repository.UsuarioRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UsuarioRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    //
    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */

    val listaUsuarios: LiveData<List<Usuario>> = repository.listaUsuarios.asLiveData()

    fun insert(usuario: Usuario) = viewModelScope.launch {
        repository.insert(usuario)
    }
}

class LoginViewModelFactory(private val repository: UsuarioRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}