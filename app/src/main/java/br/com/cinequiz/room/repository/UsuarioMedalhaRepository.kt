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

    suspend fun atualizaContadorMedalha(idUsuario: String, pontuacao: Int, idMedalha: String) {
        usuarioMedalhaDao.atualizaContadorMedalha(idUsuario, pontuacao, idMedalha)
    }

    suspend fun atualizaContadorMedalha(
        idUsuario: String,
        pontuacao: Int,
        idMedalha: String,
        flag: Boolean
    ) {
        usuarioMedalhaDao.atualizaContadorMedalha(idUsuario, pontuacao, idMedalha, flag)
    }

    suspend fun atualizaFlagMedalha(idUsuario: String, flag: Boolean, idMedalha: String) {
        usuarioMedalhaDao.atualizaFlagMedalha(idUsuario, flag, idMedalha)
    }

    suspend fun selecionaContadorMedalha(idUsuario: String, idMedalha: String): UsuarioMedalha {
        return usuarioMedalhaDao.selecionaContadorMedalha(idUsuario, idMedalha)
    }
}
