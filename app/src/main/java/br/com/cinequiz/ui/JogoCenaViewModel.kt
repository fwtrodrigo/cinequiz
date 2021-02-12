package br.com.cinequiz.ui


import android.app.Application
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.util.Log
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
import java.text.DecimalFormat


class JogoCenaViewModel(
    application: Application,
    private val usuarioRecordeRepository: UsuarioRecordeRepository,
    private val usuarioMedalhaRepository: UsuarioMedalhaRepository,
    private val medalhaRepository: MedalhaRepository
) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    private val totaisAcertos = ContadorFilmes()

    fun update() = viewModelScope.launch {
        val p = usuarioRecorde.popcornsCena
        Log.i("JogoCenaViewModel", usuarioRecorde.toString())
        if (p < pontuacao.value!!) {
            usuarioRecordeRepository.atualizaPontuacaoCena(usuarioId, pontuacao.value!!)
        }
    }

    var usuarioId = FirebaseAuth.getInstance().currentUser!!.uid
    var usuarioRecordeLiveData: LiveData<UsuarioRecorde> = usuarioRecordeRepository.get(usuarioId)
    lateinit var usuarioRecorde: UsuarioRecorde

    val pontuacao = MutableLiveData<Int>(0)
    var listaAlternativas = MutableLiveData<ArrayList<String>>()
    var alternativaCorreta = ""

    var filmes = mutableListOf<Filme>()
    var contadorFilme = 0

    val cronometroFinalizado = MutableLiveData<Boolean>()
    val cronometro = MutableLiveData<String>()
    val time: Long = Parametros.TEMPO_JOGO_CENA
    var timer = Timer(time)

    var animacaoResposta = MutableLiveData<Int>()
    var musicaJogoCena = MediaPlayer()
    var somRespostaCorreta = MediaPlayer()
    var somRespostaErrada = MediaPlayer()

    fun inicializaAudios() {
        musicaJogoCena = MediaPlayer.create(context, R.raw.modo_cena_musica)
        somRespostaCorreta = MediaPlayer.create(context, R.raw.resposta_correta_som)
        somRespostaErrada = MediaPlayer.create(context, R.raw.resposta_errada_som)
    }

    fun tocarMusica() {
        musicaJogoCena.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
            override fun onPrepared(som: MediaPlayer) {
                som.start();
            }
        })
    }

    fun continuarMusica() {
        musicaJogoCena.start()
    }

    fun pausarMusica() {
        musicaJogoCena.pause()
    }

    fun tocarRespostaCorreta() {
        somRespostaCorreta.start()
    }

    fun tocarRespostaErrada() {
        somRespostaErrada.start()
    }

    fun liberaAudios() {
        musicaJogoCena.release()
        somRespostaCorreta.release()
        somRespostaErrada.release()
    }

    fun gerarAlternativas() {
        val alternativasGeradas = arrayListOf(
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

    fun resultadoResposta(resposta: String) {
        if (resposta == alternativaCorreta) {
            //Toast.makeText(context, "Acertou", Toast.LENGTH_SHORT).show()
            animacaoResposta.value = Parametros.ID_RESPOSTA_CORRETA
            aumentaPontuacao(Parametros.PONTUACAO_ACERTO_JOGO_CENA)
            totaisAcertos.atualizaPontuacao(filmes[contadorFilme - 1])
        } else {
            //Toast.makeText(context, "Errou", Toast.LENGTH_SHORT).show()
            animacaoResposta.value = Parametros.ID_RESPOSTA_ERRADA
            descontaPontuacao(Parametros.PONTUACAO_ERRO_JOGO_CENA)
        }
    }

    fun aumentaPontuacao(pontosGanhos: Int) {
        pontuacao.value = pontuacao.value?.plus(pontosGanhos)
    }

    fun descontaPontuacao(pontosDescontados: Int) {
        if(pontuacao.value?.minus(pontosDescontados)!! < 0){
            pontuacao.value = 0
        }else{
            pontuacao.value = pontuacao.value?.minus(pontosDescontados)
        }
    }

    fun incrementaFilme() {
        contadorFilme++
    }

    fun gerarCenas(): String {
        return filmes[contadorFilme].imagensFilme[0].file_path
    }

    inner class Timer(miliis: Long) : CountDownTimer(miliis, 2) {
        var millisUntilFinished: Long = 0

        override fun onTick(millisUntilFinished: Long) {
            this.millisUntilFinished = millisUntilFinished
            val passTime = time + millisUntilFinished
            val f = DecimalFormat("00")
            val sec = passTime / 1000 % Parametros.CONTADOR_JOGO_CENA
            cronometro.value = f.format(sec).toString()
        }

        override fun onFinish() {
            cronometroFinalizado.value = true
        }
    }

    suspend fun atualizaContadoresUsuario() {
        if (totaisAcertos.totalAcertos > 0) {
            atualizaContadorMedalha(Parametros.CONTADOR_TOTAL_500, totaisAcertos.totalAcertos)
            atualizaContadorMedalha(Parametros.CONTADOR_TOTAL_1500, totaisAcertos.totalAcertos)
            atualizaContadorMedalha(Parametros.CONTADOR_TOTAL_3000, totaisAcertos.totalAcertos)
            atualizaContadorMedalha(Parametros.CONTADOR_CENA_200, totaisAcertos.totalAcertos)
            atualizaContadorMedalha(Parametros.CONTADOR_CENA_1000, totaisAcertos.totalAcertos)
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

class JogoCenaViewModelFactory(
    val application: Application,
    private val usuarioRecordeRepository: UsuarioRecordeRepository,
    private val usuarioMedalhaRepository: UsuarioMedalhaRepository,
    private val medalhaRepository: MedalhaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JogoCenaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JogoCenaViewModel(
                application, usuarioRecordeRepository,
                usuarioMedalhaRepository,
                medalhaRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}