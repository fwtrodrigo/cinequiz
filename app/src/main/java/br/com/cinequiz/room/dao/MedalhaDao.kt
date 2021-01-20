package br.com.cinequiz.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.cinequiz.domain.Medalha
import br.com.cinequiz.domain.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface MedalhaDao {

    @Query("SELECT * FROM medalha")
    fun selecionaTodasMedalhas(): Flow<List<Medalha>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insereMedalha(medalha: Medalha)
}
