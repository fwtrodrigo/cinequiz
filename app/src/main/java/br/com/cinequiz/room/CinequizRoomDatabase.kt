package br.com.cinequiz.room

import android.content.Context
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.cinequiz.domain.*
import br.com.cinequiz.room.dao.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(
    entities = arrayOf(
        Usuario::class,
        Medalha::class,
        UsuarioMedalha::class,
        UsuarioRecorde::class
    ), version = 1, exportSchema = false
)
public abstract class CinequizRoomDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun medalhaDao(): MedalhaDao
    abstract fun usuarioRecordeDao(): UsuarioRecordeDao
    abstract fun usuarioMedalhaDao(): UsuarioMedalhaDao

    private class CinequizDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var usuarioDao = database.usuarioDao()
                    var usuarioRecordeDao = database.usuarioRecordeDao()

                    Log.i("CinequizRoomDatabase", "Executando CinequizDatabaseCallback")
                    var usuario = Usuario("HAL9000", "ADÃO")
                    usuarioDao.insereUsuario(usuario)

                    var usuarioRecorde = UsuarioRecorde(0, usuario.id, 0, 0)
                    usuarioRecordeDao.insereRecordesUsuario(usuarioRecorde)
                }
            }

            INSTANCE?.let { database ->
                scope.launch {
                    var medalhaDao = database.medalhaDao()
                    var usuarioMedalhaDao = database.usuarioMedalhaDao()

                    Log.i("CinequizRoomDatabase", "Preenchendo tabela de medalhas")
                    var medalha = Medalha("ANOS_80", "Anos 80", "Acertou 100 filmes dos anos 80", 100)
                    var medalha2 = Medalha("ANOS_90", "Anos 90", "Acertou 100 filmes dos anos 90", 100)
                    var medalha3 = Medalha("ANOS_00", "Anos 2000", "Acertou 100 filmes dos anos 2000", 100)
                    medalhaDao.insereMedalha(medalha)
                    medalhaDao.insereMedalha(medalha2)
                    medalhaDao.insereMedalha(medalha3)
                    var usuarioMedalha = UsuarioMedalha(0, "ANOS_80", "HAL9000", 0, false)
                    usuarioMedalhaDao.insereUsuarioMedalha(usuarioMedalha)
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
