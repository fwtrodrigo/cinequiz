package br.com.cinequiz.ui

import android.app.Application
import androidx.lifecycle.*
import br.com.cinequiz.domain.*
import br.com.cinequiz.room.dao.UsuarioMedalhaDao
import br.com.cinequiz.room.repository.UsuarioMedalhaRepository
import br.com.cinequiz.room.repository.UsuarioRecordeRepository
import br.com.cinequiz.room.repository.UsuarioRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

class MedalhaViewModel (private val usuarioMedalhaRepository: UsuarioMedalhaRepository, private val usuarioRecordeRepository: UsuarioRecordeRepository) : ViewModel() {

    fun selecionaMedalhasPossiveis(idUsuario: String) = usuarioMedalhaRepository.selecionaMedalhasPossiveis(idUsuario)

    fun selecionaRecordeUsuario(idUsuario: String) = usuarioRecordeRepository.get(idUsuario)

    suspend fun selecionaMedalhasDisponiveis(idUsuario: String): List<UsuarioMedalhaJoin> {
        return usuarioMedalhaRepository.selecionaMedalhasDisponiveis(idUsuario)
    }
}

class MedalhaViewModelFactory(private val umRepository: UsuarioMedalhaRepository, private val urRepository: UsuarioRecordeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MedalhaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MedalhaViewModel(umRepository, urRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}