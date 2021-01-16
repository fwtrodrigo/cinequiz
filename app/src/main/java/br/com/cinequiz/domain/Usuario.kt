package br.com.cinequiz.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario")
data class Usuario(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id_usuario: String,

    @ColumnInfo(name = "nome")
    var nome: String,

)
