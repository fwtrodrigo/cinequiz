package br.com.cinequiz.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuario_medalha",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["id_usuario"]
        ), ForeignKey(
            entity = Medalha::class,
            parentColumns = ["id"],
            childColumns = ["id_medalha"]
        )]
)

data class UsuarioMedalha(

    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "id_medalha")
    var id_medalha: String,

    @ColumnInfo(name = "id_usuario")
    var id_usuario: String,

    @ColumnInfo(name = "contador")
    var contador: Int,

    @ColumnInfo(name = "flag")
    var flag: Boolean
)

data class UsuarioMedalhaJoin(var id: Int, var titulo: String, var descricao: String, var flag: Boolean)