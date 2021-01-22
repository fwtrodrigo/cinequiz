package br.com.cinequiz.room

import android.app.Application
import br.com.cinequiz.room.repository.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class CinequizApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { CinequizRoomDatabase.getDatabase(this, applicationScope) }
    val repositoryUsuario by lazy { UsuarioRepository(database.usuarioDao()) }
    val repositoryMedalha by lazy { MedalhaRepository(database.medalhaDao()) }
    val repositoryUsuarioRecorde by lazy { UsuarioRecordeRepository(database.usuarioRecordeDao()) }
    val repositoryUsuarioMedalha by lazy { UsuarioMedalhaRepository(database.usuarioMedalhaDao()) }
}
