package br.com.cinequiz.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuario_contador",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["id_usuario"]
        ), ForeignKey(
            entity = Contador::class,
            parentColumns = ["id"],
            childColumns = ["id_contador"]
        )]
)

data class UsuarioContador(

    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "id_usuario")
    var id_usuario: String,

    @ColumnInfo(name = "id_contador")
    var id_contador: String,

    @ColumnInfo(name = "quantidade")
    var quantidade: Int

)