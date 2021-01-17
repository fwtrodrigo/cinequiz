package br.com.cinequiz.room.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import br.com.cinequiz.domain.UsuarioMedalha
import br.com.cinequiz.domain.UsuarioMedalhaJoin
import br.com.cinequiz.room.dao.UsuarioMedalhaDao

class UsuarioMedalhaRepository(private val usuarioMedalhaDao: UsuarioMedalhaDao) {

    //val usuarioMedalha: Flow<UsuarioMedalha> = usuarioMedalhaDao.selecionaMedalhasPorUsuario(idUsuario: String)

    fun selecionaMedalhasPossiveis(idUsuario: String): LiveData<List<UsuarioMedalhaJoin>> {
        return usuarioMedalhaDao.selecionaMedalhasPossiveis(idUsuario)
    }

    fun selecionaMedalhasNaoConquistadas(idUsuario: String): LiveData<List<UsuarioMedalha>> {
        return usuarioMedalhaDao.selecionaMedalhasNaoConquistadas(idUsuario)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread

    suspend fun insert(usuarioMedalha: UsuarioMedalha) {
        usuarioMedalhaDao.insereUsuarioMedalha(usuarioMedalha)
    }

    suspend fun atualizaContadorMedalha(idUsuario: String, pontuacao: Int){
        usuarioMedalhaDao.atualizaContadorMedalha(idUsuario, pontuacao)
    }

    suspend fun atualizaFlagMedalha(idUsuario: String, flag: Boolean){
        usuarioMedalhaDao.atualizaFlagMedalha(idUsuario,flag)
    }
}
