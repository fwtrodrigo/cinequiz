package br.com.cinequiz.room.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.Update
import br.com.cinequiz.domain.UsuarioRecorde
import br.com.cinequiz.room.dao.UsuarioRecordeDao

class UsuarioRecordeRepository(private val usuarioRecordeDao: UsuarioRecordeDao) {

    fun get(idUsuario: String): LiveData<UsuarioRecorde> {
        return usuarioRecordeDao.selecionaRecordesPorUsuario(idUsuario)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(usuarioRecorde: UsuarioRecorde) {
        usuarioRecordeDao.insereRecordesUsuario(usuarioRecorde)
    }

    suspend fun atualizaPontuacaoDica(usuarioId: String, pontuacao: Int) {
        usuarioRecordeDao.atualizaPontuacaoDica(usuarioId, pontuacao)
    }

    suspend fun atualizaPontuacaoCena(usuarioId: String, pontuacao: Int) {
        usuarioRecordeDao.atualizaPontuacaoCena(usuarioId, pontuacao)
    }

}
