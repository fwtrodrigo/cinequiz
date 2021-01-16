package br.com.cinequiz.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.cinequiz.domain.Recorde
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordeDao {

    @Query("SELECT * FROM recorde")
    fun selecionaTodosRecordes(): Flow<List<Recorde>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insereRecorde(recorde: Recorde)
}