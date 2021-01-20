package br.com.cinequiz.ui

import android.app.Application
import androidx.lifecycle.*
import br.com.cinequiz.domain.Medalha
import br.com.cinequiz.domain.Usuario
import br.com.cinequiz.domain.UsuarioMedalha
import br.com.cinequiz.domain.UsuarioMedalhaJoin
import br.com.cinequiz.room.dao.UsuarioMedalhaDao
import br.com.cinequiz.room.repository.UsuarioMedalhaRepository
import br.com.cinequiz.room.repository.UsuarioRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

class MedalhaViewModel (private val usuarioMedalhaRepository: UsuarioMedalhaRepository) : ViewModel() {

    fun selecionaMedalhasPossiveis(idUsuario: String) = usuarioMedalhaRepository.selecionaMedalhasPossiveis(idUsuario)

    fun selecionaMedalhasNaoConquistadas(idUsuario: String) = usuarioMedalhaRepository.selecionaMedalhasNaoConquistadas(idUsuario)

}

class MedalhaViewModelFactory(private val repository: UsuarioMedalhaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MedalhaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MedalhaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}