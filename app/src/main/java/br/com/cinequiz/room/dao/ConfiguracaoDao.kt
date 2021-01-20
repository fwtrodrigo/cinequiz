package br.com.cinequiz.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.cinequiz.domain.Configuracao
import kotlinx.coroutines.flow.Flow

@Dao
interface ConfiguracaoDao {

    @Query("SELECT * FROM configuracao")
    fun selecionaTodasConfiguracoes(): Flow<List<Configuracao>>

    @Query("UPDATE configuracao SET notificacoes = :notificacoes WHERE id_usuario = :usuarioId")
    suspend fun atualizaNotificacoes(usuarioId: String, notificacoes: Boolean)

    @Query("UPDATE configuracao SET musica = :musica WHERE id_usuario = :usuarioId")
    suspend fun atualizaMusica(usuarioId: String, musica: Boolean)

    @Query("UPDATE configuracao SET sons = :sons WHERE id_usuario = :usuarioId")
    suspend fun atualizaSons(usuarioId: String, sons: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insereconfiguracao(configuracao: Configuracao)
}
