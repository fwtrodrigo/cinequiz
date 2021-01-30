package br.com.cinequiz.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.com.cinequiz.domain.Medalha
import br.com.cinequiz.domain.Usuario
import br.com.cinequiz.domain.UsuarioMedalha
import br.com.cinequiz.domain.UsuarioRecorde
import br.com.cinequiz.room.repository.MedalhaRepository
import br.com.cinequiz.room.repository.UsuarioMedalhaRepository
import br.com.cinequiz.room.repository.UsuarioRecordeRepository
import br.com.cinequiz.room.repository.UsuarioRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val medalhaRepository: MedalhaRepository,
    private val usuarioMedalhaRepository: UsuarioMedalhaRepository,
    private val usuarioRecordeRepository: UsuarioRecordeRepository
) : ViewModel() {
    
    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    //
    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */

    public fun selectMedalhas(): LiveData<List<Medalha>> {
        return medalhaRepository.listaMedalhas
    }

    private fun insertUsuario(usuario: Usuario) = viewModelScope.launch {
        usuarioRepository.insert(usuario)
    }

    private fun insertUsuarioMedalha(usuarioMedalha: UsuarioMedalha) = viewModelScope.launch {
        usuarioMedalhaRepository.insert(usuarioMedalha)
    }

    private fun insertUsuarioRecorde(usuarioRecorde: UsuarioRecorde) = viewModelScope.launch {
        usuarioRecordeRepository.insert(usuarioRecorde)
    }

    private fun rotinaNovoUsuario(idUsuario: String, nome: String) = viewModelScope.launch {
        insertUsuario(Usuario(idUsuario, nome))
        insertUsuarioRecorde(UsuarioRecorde(0, idUsuario, 0, 0))

        val medalhas = medalhaRepository.listaMedalhas

        medalhas.value?.forEach { medalha ->
            Log.i("LOGINVIEWMODELMEDALHA", "$medalha")
            insertUsuarioMedalha(UsuarioMedalha(0, medalha.id, idUsuario, 0, false))
        }
    }

    public fun buscaUsuario(idUsuario: String) = usuarioRepository.selecionaPorID(idUsuario)

    fun implantaUsuario(idUsuario: String, nome: String) {
        viewModelScope.launch {
            val resp = usuarioRepository.selecionaPorID(idUsuario)
            /*fixme*/
            if (resp.value?.id == null) {
                Log.i("LOGINVIEWMODEL", "ACESSANDO ROTINA USUARIO")
                rotinaNovoUsuario(idUsuario, nome)
            }
        }
    }
}

class LoginViewModelFactory(
    private val usuarioRepository: UsuarioRepository,
    private val medalhaRepository: MedalhaRepository,
    private val usuarioMedalhaRepository: UsuarioMedalhaRepository,
    private val usuarioRecordeRepository: UsuarioRecordeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(
                usuarioRepository,
                medalhaRepository,
                usuarioMedalhaRepository,
                usuarioRecordeRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}