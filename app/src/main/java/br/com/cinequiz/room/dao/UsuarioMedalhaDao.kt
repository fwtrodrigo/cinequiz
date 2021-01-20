package br.com.cinequiz.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
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

    @Query("UPDATE usuario_medalha SET contador = :pontuacao WHERE ID_USUARIO = :idUsuario")
    suspend fun atualizaContadorMedalha(idUsuario: String, pontuacao: Int)

    @Query("UPDATE usuario_medalha SET flag = :flag WHERE ID_USUARIO = :idUsuario")
    suspend fun atualizaFlagMedalha(idUsuario: String, flag: Boolean)

}
