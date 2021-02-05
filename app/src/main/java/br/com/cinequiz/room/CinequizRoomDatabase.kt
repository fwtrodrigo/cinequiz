package br.com.cinequiz.room

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.cinequiz.domain.*
import br.com.cinequiz.room.dao.*
import kotlinx.coroutines.CoroutineScope
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
                    inicializaMedalhas(database)
                }
            }
        }

        private suspend fun inicializaMedalhas(database: CinequizRoomDatabase) {
            var medalhaDao = database.medalhaDao()

            Log.i("CinequizRoomDatabase", "Preenchendo tabela de medalhas")
            var medalha1 =
                Medalha("TOTAL_500", "Woody Allen", "Acertou 500 filmes", 500)
            var medalha2 =
                Medalha("TOTAL_1500", "Martin Scorsese", "Acertou 1500 filmes", 1500)
            var medalha3 =
                Medalha("TOTAL_3000", "Steven Spielberg", "Acertou 3000 filmes", 3000)
            var medalha4 =
                Medalha("CENA_200", "Mad Max", "Acertou 200 filmes no modo Cena", 200)
            var medalha5 =
                Medalha("CENA_1000", "Velozes e Furiosos", "Acertou 1000 filmes no modo Cena", 1000)
            var medalha6 =
                Medalha("DICA_200", "Professor Xavier", "Acertou 200 filmes do modo Dica", 200)
            var medalha7 =
                Medalha("DICA_1000", "Tony Stark", "Acertou 1000 filmes do modo Dica", 1000)
            var medalha8 =
                Medalha("ANOS_70", "Mestre Yoda", "Acertou 200 filmes dos anos 70", 200)
            var medalha9 =
                Medalha("ANOS_80", "Rambo", "Acertou 100 filmes dos anos 80", 100)
            var medalha10 =
                Medalha("ANOS_90", "Rei Leão", "Acertou 200 filmes dos anos 90", 200)
            var medalha11 =
                Medalha("ANOS_00", "O Senhor dos Anéis", "Acertou 300 filmes dos anos 2000", 300)
            var medalha12 =
                Medalha("ANOS_10", "Saga Vingadores", "Acertou 400 filmes dos anos 2010", 400)

            medalhaDao.insereMedalha(medalha1)
            medalhaDao.insereMedalha(medalha2)
            medalhaDao.insereMedalha(medalha3)
            medalhaDao.insereMedalha(medalha4)
            medalhaDao.insereMedalha(medalha5)
            medalhaDao.insereMedalha(medalha6)
            medalhaDao.insereMedalha(medalha7)
            medalhaDao.insereMedalha(medalha8)
            medalhaDao.insereMedalha(medalha9)
            medalhaDao.insereMedalha(medalha10)
            medalhaDao.insereMedalha(medalha11)
            medalhaDao.insereMedalha(medalha12)
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
