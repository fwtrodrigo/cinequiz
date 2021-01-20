package br.com.cinequiz.room.repository

import androidx.annotation.WorkerThread
import br.com.cinequiz.domain.Configuracao
import br.com.cinequiz.room.dao.ConfiguracaoDao
import kotlinx.coroutines.flow.Flow

class ConfiguracaoRepository(private val configuracaoDao: ConfiguracaoDao) {

    val listaConfiguracoes: Flow<List<Configuracao>> = configuracaoDao.selecionaTodasConfiguracoes()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(configuracao: Configuracao) {
        configuracaoDao.insereconfiguracao(configuracao)
    }

    suspend fun updateNotificacoes(usuarioId: String, notificacoes: Boolean) {
        configuracaoDao.atualizaNotificacoes(usuarioId, notificacoes)
    }

    suspend fun updateMusica(usuarioId: String, musica: Boolean) {
        configuracaoDao.atualizaMusica(usuarioId, musica)
    }

    suspend fun updateSons(usuarioId: String, sons: Boolean) {
        configuracaoDao.atualizaSons(usuarioId, sons)
    }

}
