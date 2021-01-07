package br.com.cinequiz.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.cinequiz.domain.Usuario

@Dao
interface UsuarioDao {

    @Query("SELECT id FROM usuario where id = :idUsuario")
    fun verificaCadastroUsuario(idUsuario: String): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insereUsuario(usuario: Usuario)

}
