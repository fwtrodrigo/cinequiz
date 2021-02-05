package br.com.cinequiz.ui


import android.app.Application
import android.media.MediaPlayer
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.lifecycle.*
import br.com.cinequiz.R
import br.com.cinequiz.domain.ContadorFilmes
import br.com.cinequiz.domain.Filme
import br.com.cinequiz.domain.Parametros
import br.com.cinequiz.domain.UsuarioRecorde
import br.com.cinequiz.room.repository.MedalhaRepository
import br.com.cinequiz.room.repository.UsuarioMedalhaRepository
import br.com.cinequiz.room.repository.UsuarioRecordeRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class JogoDicaViewModel(
    application: Application,
    private val usuarioRecordeRepository: UsuarioRecordeRepository,
    private val usuarioMedalhaRepository: UsuarioMedalhaRepository,
    private val medalhaRepository: MedalhaRepository
) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    private val totaisAcertos = ContadorFilmes()

    var usuarioId = FirebaseAuth.getInstance().currentUser!!.uid
    var usuarioRecordeLiveData: LiveData<UsuarioRecorde> = usuarioRecordeRepository.get(usuarioId)


    fun update() = viewModelScope.launch {
        val p = usuarioRecorde.popcornsDica
        Log.i("JOGODICAVIEWMODEL", usuarioRecorde.toString())
        if (p < pontuacao.value!!) {
            usuarioRecordeRepository.atualizaPontuacaoDica(usuarioId, pontuacao.value!!)
        }
    }

    lateinit var usuarioRecorde: UsuarioRecorde
    val pontuacao = MutableLiveData<Int>(Parametros.PONTUACAO_INICIAL_JOGO_DICA)

    var listaDicas = MutableLiveData<ArrayList<String>>()
    var listaDicasGeradas = ArrayList<String>()
    var contadorDica = 0

    var listaAlternativas = MutableLiveData<ArrayList<String>>()
    var alternativaCorreta = ""

    var filmes = mutableListOf<Filme>()
    var contadorFilme = 0

    var animacaoResposta = MutableLiveData<Int>()
    var musicaJogoDica = MediaPlayer()
    var somRespostaCorreta = MediaPlayer()
    var somRespostaErrada = MediaPlayer()

    fun inicializaAudios() {
        musicaJogoDica = MediaPlayer.create(context, R.raw.modo_dica_musica)
        musicaJogoDica.isLooping = true
        somRespostaCorreta = MediaPlayer.create(context, R.raw.resposta_correta_som)
        somRespostaErrada = MediaPlayer.create(context, R.raw.resposta_errada_som)
    }

    fun liberaAudios() {
        musicaJogoDica.release()
        somRespostaCorreta.release()
        somRespostaErrada.release()
    }

    fun tocarMusica() {
        musicaJogoDica.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
            override fun onPrepared(som: MediaPlayer) {
                som.start();
            }
        })
    }

    fun tocarRespostaCorreta() {
        somRespostaCorreta.start()
    }

    fun tocarRespostaErrada() {
        somRespostaErrada.start()
    }

    fun gerarDicas() {

        contadorDica = 0

        listaDicas.value = arrayListOf()
        listaDicasGeradas = arrayListOf(
            "${filmes[contadorFilme].pessoasFilme[0].name} fez parte do meu elenco.",
            "Fui produzido pelo estúdio ${filmes[contadorFilme].production_companies[0].name}.",
            "Minha estreia foi em ${filmes[contadorFilme].formataDataLancamento()}.",
            "Tenho um personagem chamado ${filmes[contadorFilme].pessoasFilme[0].character}.",
            "${filmes[contadorFilme].pessoasFilme[1].character} foi um dos meus personagens.",

            )

        listaDicasGeradas.shuffle()
        for (i in 0 until listaDicasGeradas.size) {
            listaDicasGeradas[i] = (i + 1).toString() + " - " + listaDicasGeradas[i]
        }

        proximaDica(0)
    }

    fun gerarAlternativas() {
        var alternativasGeradas = arrayListOf(
            filmes[contadorFilme].title,
            filmes[contadorFilme].filmesSimilares[0].title,
            filmes[contadorFilme].filmesSimilares[1].title,
            filmes[contadorFilme].filmesSimilares[2].title
        )

        alternativasGeradas.shuffle()
        listaAlternativas.value = alternativasGeradas

        alternativasGeradas.forEachIndexed { index, alternativa ->
            if (alternativa == filmes[contadorFilme].title) alternativaCorreta = "btn" + (index + 1)
        }
    }

    fun proximaDica(pontosDescontados: Int) {

        if (contadorDica == listaDicasGeradas.size) {
            val toast = Toast.makeText(context, "Sem dicas disponíveis", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP, 0, 450)
            toast.show()
        } else {
            var listaAuxiliar = listaDicas.value
            listaAuxiliar!!.add((listaDicasGeradas.get(contadorDica)))
            listaDicas.value = listaAuxiliar
            contadorDica++
            descontaPontuacao(pontosDescontados)
        }
    }

    fun resultadoResposta(resposta: String) {
        if (resposta == alternativaCorreta) {
            animacaoResposta.value = Parametros.ID_RESPOSTA_CORRETA
            aumentaPontuacao(Parametros.PONTUACAO_ACERTO_JOGO_DICA)
            totaisAcertos.atualizaPontuacao(filmes[contadorFilme - 1])
        } else {
            descontaPontuacao(Parametros.PONTUACAO_ERRO_JOGO_DICA)
            animacaoResposta.value = Parametros.ID_RESPOSTA_ERRADA
        }
    }

    fun aumentaPontuacao(pontosGanhos: Int) {
        pontuacao.value = pontuacao.value?.plus(pontosGanhos)
    }

    fun descontaPontuacao(pontosDescontados: Int) {
        pontuacao.value = pontuacao.value?.minus(pontosDescontados)
    }

    fun incrementaFilme() {
        contadorFilme++
    }

    suspend fun atualizaContadoresUsuario() {
        if (totaisAcertos.totalAcertos > 0) {
            atualizaContadorMedalha(Parametros.CONTADOR_TOTAL_500, totaisAcertos.totalAcertos)
            atualizaContadorMedalha(Parametros.CONTADOR_TOTAL_1500, totaisAcertos.totalAcertos)
            atualizaContadorMedalha(Parametros.CONTADOR_TOTAL_3000, totaisAcertos.totalAcertos)
            atualizaContadorMedalha(Parametros.CONTADOR_DICA_200, totaisAcertos.totalAcertos)
            atualizaContadorMedalha(Parametros.CONTADOR_DICA_1000, totaisAcertos.totalAcertos)
            Log.d("ENTROU NOS TOTAIS", "TOTAIS")
        }

        if (totaisAcertos.totalAnos70 > 0) {
            atualizaContadorMedalha(Parametros.CONTADOR_ANOS_70, totaisAcertos.totalAnos70)
            Log.d("ENTROU NOS 70", "ANOS 70")
        }

        if (totaisAcertos.totalAnos80 > 0) {
            atualizaContadorMedalha(Parametros.CONTADOR_ANOS_80, totaisAcertos.totalAnos80)
            Log.d("ENTROU NOS 80", "ANOS 80")
        }

        if (totaisAcertos.totalAnos90 > 0) {
            atualizaContadorMedalha(Parametros.CONTADOR_ANOS_90, totaisAcertos.totalAnos90)
            Log.d("ENTROU NOS 90", "ANOS 90")
        }

        if (totaisAcertos.totalAnos00 > 0) {
            atualizaContadorMedalha(Parametros.CONTADOR_ANOS_00, totaisAcertos.totalAnos00)
            Log.d("ENTROU NOS 00", "ANOS 00")
        }

        if (totaisAcertos.totalAnos10 > 0) {
            atualizaContadorMedalha(Parametros.CONTADOR_ANOS_10, totaisAcertos.totalAnos10)
            Log.d("ENTROU NOS 10", "ANOS 10")
        }

        if (totaisAcertos.totalAcertos == 0) {
            Log.d("ATUALIZACONTADOR", "NAO TEVE ACERTO")
        }
    }

    private suspend fun atualizaContadorMedalha(idMedalha: String, acertos: Int) {

        val contador = usuarioMedalhaRepository.selecionaContadorMedalha(
            usuarioId,
            idMedalha
        )

        if (contador != null) {

            val totalBD = contador.contador
            Log.d("TOTALBD", totalBD.toString())
            val totalPartida = acertos ?: 0
            val totalAtual = totalPartida + totalBD
            Log.d("TOTALATUAL", totalAtual.toString())

            controleGanhoMedalha(totalAtual, idMedalha)
        }

    }


    private suspend fun controleGanhoMedalha(totalAtual: Int, idMedalha: String) {

        val medalha = medalhaRepository.selecionaPorId(idMedalha)

        if (medalha == null) {
            return
        }

        val requisito = medalha.requisito

        if (totalAtual >= requisito) {
            usuarioMedalhaRepository.atualizaContadorMedalha(
                usuarioId,
                totalAtual,
                idMedalha,
                true
            )

        } else {
            usuarioMedalhaRepository.atualizaContadorMedalha(
                usuarioId,
                totalAtual,
                idMedalha,
            )
        }
    }


}

class JogoDicaViewModelFactory(
    val application: Application,
    private val usuarioRecordeRepository: UsuarioRecordeRepository,
    private val usuarioMedalhaRepository: UsuarioMedalhaRepository,
    private val medalhaRepository: MedalhaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JogoDicaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JogoDicaViewModel(
                application,
                usuarioRecordeRepository,
                usuarioMedalhaRepository,
                medalhaRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}