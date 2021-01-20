package br.com.cinequiz.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuario_recorde",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["id_usuario"]
        )]
)

data class UsuarioRecorde(

    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "id_usuario")
    var id_usuario: String,

    @ColumnInfo(name = "popcorns_cena")
    var popcornsCena: Int,

    @ColumnInfo(name = "popcorns_dica")
    var popcornsDica: Int

)
