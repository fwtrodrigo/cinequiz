package br.com.cinequiz.room.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import br.com.cinequiz.domain.UsuarioRecorde
import br.com.cinequiz.room.dao.UsuarioRecordeDao

class UsuarioRecordeRepository(private val usuarioRecordeDao: UsuarioRecordeDao) {

    //val usuarioRecorde: Flow<UsuarioRecorde> = usuarioRecordeDao.selecionaRecordesPorUsuario(idUsuario: String)

    fun get(idUsuario: String): LiveData<UsuarioRecorde> {
        return usuarioRecordeDao.selecionaRecordesPorUsuario(idUsuario)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(usuarioRecorde: UsuarioRecorde) {
        usuarioRecordeDao.insereRecordesUsuario(usuarioRecorde)
    }
}
