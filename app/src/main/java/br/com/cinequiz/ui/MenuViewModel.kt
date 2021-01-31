package br.com.cinequiz.ui

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import br.com.cinequiz.domain.*
import br.com.cinequiz.room.repository.MedalhaRepository
import br.com.cinequiz.room.repository.UsuarioMedalhaRepository
import br.com.cinequiz.room.repository.UsuarioRecordeRepository
import br.com.cinequiz.room.repository.UsuarioRepository
import br.com.cinequiz.service.Repository
import kotlinx.coroutines.launch

class MenuViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val medalhaRepository: MedalhaRepository,
    private val usuarioMedalhaRepository: UsuarioMedalhaRepository,
    private val usuarioRecordeRepository: UsuarioRecordeRepository
    ) : ViewModel() {


    fun inicializaUsuario(prefs: SharedPreferences, usuarioId: String, usuarioNome: String){
        if (prefs.getBoolean("firstrun", true)) {
            implantaUsuario(usuarioId, usuarioNome.toString())
            val editor = prefs.edit()
            editor.putBoolean("notificacoes", true)
            editor.putBoolean("musica", true)
            editor.putBoolean("sons", true)
            editor.putBoolean("firstrun", false)
            editor.apply()
        }
    }

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

    //public fun buscaUsuario(idUsuario: String) = usuarioRepository.selecionaPorID(idUsuario)

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

class MenuViewModelFactory(
    private val usuarioRepository: UsuarioRepository,
    private val medalhaRepository: MedalhaRepository,
    private val usuarioMedalhaRepository: UsuarioMedalhaRepository,
    private val usuarioRecordeRepository: UsuarioRecordeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MenuViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MenuViewModel(
                usuarioRepository,
                medalhaRepository,
                usuarioMedalhaRepository,
                usuarioRecordeRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}




