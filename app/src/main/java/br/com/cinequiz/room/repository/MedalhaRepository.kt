package br.com.cinequiz.room.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import br.com.cinequiz.domain.Medalha
import br.com.cinequiz.room.dao.MedalhaDao
import kotlinx.coroutines.flow.Flow

class MedalhaRepository(private val medalhaDao: MedalhaDao) {

    val listaMedalhas: LiveData<List<Medalha>> = medalhaDao.selecionaTodasMedalhas()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(medalha: Medalha) {
        medalhaDao.insereMedalha(medalha)
    }
}
