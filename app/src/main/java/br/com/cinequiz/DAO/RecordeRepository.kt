package br.com.cinequiz.DAO

import androidx.annotation.WorkerThread
import br.com.cinequiz.domain.Recorde
import br.com.cinequiz.domain.Usuario
import kotlinx.coroutines.flow.Flow

class RecordeRepository(private val recordeDao: RecordeDao) {

    val listaRecordes: Flow<List<Recorde>> = recordeDao.selecionaTodosRecordes()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(recorde: Recorde) {
        recordeDao.insereRecorde(recorde)
    }

}