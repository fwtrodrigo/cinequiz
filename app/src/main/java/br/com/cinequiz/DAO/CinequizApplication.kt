package br.com.cinequiz.DAO

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class CinequizApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { CinequizRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { UsuarioRepository(database.usuarioDao()) }
}
