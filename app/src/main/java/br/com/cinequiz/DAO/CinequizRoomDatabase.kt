package br.com.cinequiz.DAO

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.cinequiz.domain.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(Usuario::class), version = 1, exportSchema = false)
public abstract class CinequizRoomDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    private class CinequizDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var usuarioDao = database.usuarioDao()

                    Log.i("CinequizRoomDatabase", "Executando CinequizDatabaseCallback")
                    // Add sample words.
                    var usuario = Usuario("HAL9000", "ADÃO")
                    usuarioDao.insereUsuario(usuario)
                }
            }
        }
    }


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: CinequizRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): CinequizRoomDatabase {

            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CinequizRoomDatabase::class.java,
                    "cinequiz_database"
                ).addCallback(CinequizDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
