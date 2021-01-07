package br.com.cinequiz.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "configuracao", foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["id_usuario"]
        )]
)
data class Configuracao(

    @ColumnInfo(name = "id_usuario")
    var id_usuario: String,

    @ColumnInfo(name = "notificacoes")
    var notificacoes: Boolean,

    @ColumnInfo(name = "musica")
    var musica: Boolean,

    @ColumnInfo(name = "sons")
    var sons: Boolean,

    )