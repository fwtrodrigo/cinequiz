package br.com.cinequiz.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.cinequiz.domain.UsuarioMedalha
import br.com.cinequiz.domain.UsuarioMedalhaJoin


@Dao
interface UsuarioMedalhaDao {

    @Query(
        "SELECT um.id, m.titulo, m.descricao, um.flag FROM usuario_medalha um INNER JOIN medalha m ON um.id_medalha = m.id WHERE um.id_usuario = :idUsuario"
    )
    fun selecionaMedalhasPossiveis(idUsuario: String): LiveData<List<UsuarioMedalhaJoin>>

    @Query(
        "SELECT * FROM usuario_medalha WHERE flag = 0 and id_usuario = :idUsuario"
    )
    fun selecionaMedalhasNaoConquistadas(idUsuario: String): LiveData<List<UsuarioMedalha>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insereUsuarioMedalha(usuarioMedalha: UsuarioMedalha)

    @Query("UPDATE usuario_medalha SET contador = :pontuacao WHERE ID_USUARIO = :idUsuario AND id_medalha = :idMedalha")
    suspend fun atualizaContadorMedalha(idUsuario: String, pontuacao: Int, idMedalha: String)

    @Query("UPDATE usuario_medalha SET contador = :pontuacao, flag = :flag WHERE ID_USUARIO = :idUsuario AND id_medalha = :idMedalha")
    suspend fun atualizaContadorMedalha(idUsuario: String, pontuacao: Int, idMedalha: String, flag: Boolean)

    @Query("UPDATE usuario_medalha SET flag = :flag WHERE ID_USUARIO = :idUsuario AND id_medalha = :idMedalha")
    suspend fun atualizaFlagMedalha(idUsuario: String, flag: Boolean, idMedalha: String)

    @Query(
        "SELECT id,  id_medalha, contador, id_usuario, flag  FROM usuario_medalha  WHERE id_usuario = :idUsuario AND id_medalha = :idMedalha AND flag = 0"
    )
    suspend fun selecionaContadorMedalha(idUsuario: String, idMedalha: String): UsuarioMedalha

}
