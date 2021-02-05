package br.com.cinequiz.ui


import android.app.Application
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.util.Log
import br.com.cinequiz.R
import androidx.lifecycle.*
import br.com.cinequiz.domain.Filme
import br.com.cinequiz.domain.Parametros
import br.com.cinequiz.domain.UsuarioRecorde
import br.com.cinequiz.room.repository.UsuarioRecordeRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.text.DecimalFormat


class JogoCenaViewModel(
    application: Application,
    private val usuarioRecordeRepository: UsuarioRecordeRepository
) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext


    fun update() = viewModelScope.launch {
        val p = usuarioRecorde.popcornsCena
        Log.i("JogoCenaViewModel", usuarioRecorde.toString())
        if (p < pontuacao.value!!) {
            usuarioRecordeRepository.atualizaPontuacaoCena(usuarioId, pontuacao.value!!)
        }
    }

    var usuarioId = FirebaseAuth.getInstance().currentUser!!.uid
    var usuarioRecordeLiveData: LiveData<UsuarioRecorde> = usuarioRecordeRepository.get(usuarioId)
    lateinit var usuarioRecorde : UsuarioRecorde

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

    fun inicializaAudios(){
        musicaJogoCena = MediaPlayer.create(context, R.raw.modo_cena_musica)
        somRespostaCorreta = MediaPlayer.create(context, R.raw.resposta_correta_som)
        somRespostaErrada = MediaPlayer.create(context, R.raw.resposta_errada_som)
    }

    fun tocarMusica(){
        musicaJogoCena.start()
    }

    fun pausarMusica(){
        musicaJogoCena.pause()
    }

    fun tocarRespostaCorreta(){
        somRespostaCorreta.start()
    }

    fun tocarRespostaErrada(){
        somRespostaErrada.start()
    }

    fun pararMusica(){
        musicaJogoCena.stop()
    }

    fun liberaAudios(){
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
        } else {
            //Toast.makeText(context, "Errou", Toast.LENGTH_SHORT).show()
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

    fun gerarCenas(): String {
        return filmes[contadorFilme].imagensFilme[0].file_path
    }

    inner class Timer(miliis:Long) : CountDownTimer(miliis,2){
        var millisUntilFinished:Long = 0

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
}

class JogoCenaViewModelFactory(
    val application: Application,
    private val repository: UsuarioRecordeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JogoCenaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JogoCenaViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}