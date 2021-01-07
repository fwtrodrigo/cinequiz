package br.com.cinequiz.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "medalha", foreignKeys = [
        ForeignKey(
            entity = Contador::class,
            parentColumns = ["id"],
            childColumns = ["id_contador"]
        )]
)

data class Medalha(

    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "titulo")
    var titulo: String,

    @ColumnInfo(name = "descricao")
    var descricao: String,

    @ColumnInfo(name = "requisito")
    var requisito: Int,

    @ColumnInfo(name = "id_contador")
    var id_contador: String,

    )

