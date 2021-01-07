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
        ), ForeignKey(
            entity = Recorde::class,
            parentColumns = ["id"],
            childColumns = ["id_recorde"]
        )]
)

data class UsuarioRecorde(

    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "id_recorde")
    var id_recorde: Int,

    @ColumnInfo(name = "id_usuario")
    var id_usuario: String,

    @ColumnInfo(name = "popcorns")
    var popcorns: Int
)
