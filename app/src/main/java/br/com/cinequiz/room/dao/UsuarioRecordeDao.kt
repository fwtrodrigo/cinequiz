package br.com.cinequiz.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.cinequiz.domain.UsuarioRecorde
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioRecordeDao {

    @Query("SELECT * FROM usuario_recorde WHERE id_usuario = :idUsuario")
    fun selecionaRecordesPorUsuario(idUsuario: String): LiveData<UsuarioRecorde>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insereRecordesUsuario(usuarioRecorde: UsuarioRecorde)

    @Query("UPDATE usuario_recorde SET popcorns_dica = :pontuacao WHERE id_usuario = :idUsuario")
    suspend fun atualizaPontuacaoDica(idUsuario: String, pontuacao: Int)

    @Query("UPDATE usuario_recorde SET popcorns_cena = :pontuacao WHERE id_usuario = :idUsuario")
    suspend fun atualizaPontuacaoCena(idUsuario: String, pontuacao: Int)

}
