package br.com.cinequiz.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "medalha"
)

data class Medalha(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "titulo")
    var titulo: String,

    @ColumnInfo(name = "descricao")
    var descricao: String,

    @ColumnInfo(name = "requisito")
    var requisito: Int,

    )

