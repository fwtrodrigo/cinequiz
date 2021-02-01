package br.com.cinequiz.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.cinequiz.domain.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Query("SELECT * FROM usuario")
    fun selecionaTodosUsuarios(): Flow<List<Usuario>>

    @Query("SELECT * FROM usuario where id = :idUsuario")
    fun selecionaPorID(idUsuario: String): LiveData<Usuario>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insereUsuario(usuario: Usuario)

}
