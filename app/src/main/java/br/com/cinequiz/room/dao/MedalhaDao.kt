package br.com.cinequiz.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.cinequiz.domain.Medalha

@Dao
interface MedalhaDao {

    @Query("SELECT * FROM medalha")
    fun selecionaTodasMedalhas(): LiveData<List<Medalha>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insereMedalha(medalha: Medalha)

    @Query("SELECT * FROM medalha WHERE id = :idMedalha")
    suspend fun selecionaPorId(idMedalha: String): Medalha

}
