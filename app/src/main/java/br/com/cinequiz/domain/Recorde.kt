package br.com.cinequiz.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recorde")
data class Recorde(

    @PrimaryKey(autoGenerate = false)
    var id: String,

    )
