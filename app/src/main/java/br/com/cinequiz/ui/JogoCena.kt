package br.com.cinequiz.ui


import android.animation.Animator
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import br.com.cinequiz.R
import br.com.cinequiz.adapters.CancelaJogoDialogAdapter
import br.com.cinequiz.adapters.ResultadoDialogAdapter
import br.com.cinequiz.databinding.ActivityJogoCenaBinding
import br.com.cinequiz.domain.Parametros
import br.com.cinequiz.domain.SingletonListaFilmes
import br.com.cinequiz.room.CinequizApplication
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso


class JogoCena : AppCompatActivity() {

    private lateinit var binding: ActivityJogoCenaBinding
    private lateinit var prefs: SharedPreferences
    private lateinit var mAuth: FirebaseAuth
    private lateinit var animacaoRespostaCorreta: LottieAnimationView
    private lateinit var animacaoRespostaErrada: LottieAnimationView

    private val JogoCenaViewModel: JogoCenaViewModel by viewModels {
        JogoCenaViewModelFactory(
            application,
            (application as CinequizApplication).repositoryUsuarioRecorde
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJogoCenaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listaFilmes = SingletonListaFilmes.filmes

        mAuth = FirebaseAuth.getInstance()
        val idUsuario = mAuth.currentUser?.uid
        prefs = getSharedPreferences("userPrefs_$idUsuario", MODE_PRIVATE)

        animacaoRespostaCorreta = binding.itemCardCena.animacaoRespostaCorreta
        animacaoRespostaErrada = binding.itemCardCena.animacaoRespostaErrada
        inicializaAnimacao(animacaoRespostaCorreta, Parametros.ID_RESPOSTA_CORRETA)
        inicializaAnimacao(animacaoRespostaErrada, Parametros.ID_RESPOSTA_ERRADA)

        JogoCenaViewModel.inicializaAudios()
        JogoCenaViewModel.filmes = listaFilmes

        if (prefs.getBoolean("musica", true)) {
            JogoCenaViewModel.tocarMusica()
        }

        novaPartida()


        binding.includeJogoCenaBotoes.imageButtonAlternativas1.setOnClickListener {
            vibrarBotão()
            selecaoAlternativa("btn1")
        }

        binding.includeJogoCenaBotoes.imageButtonAlternativas2.setOnClickListener {
            vibrarBotão()
            selecaoAlternativa("btn2")
        }

        binding.includeJogoCenaBotoes.imageButtonAlternativas3.setOnClickListener {
            vibrarBotão()
            selecaoAlternativa("btn3")
        }

        binding.includeJogoCenaBotoes.imageButtonAlternativas4.setOnClickListener {
            vibrarBotão()
            selecaoAlternativa("btn4")
        }

    }

    fun novaPartida() {

        JogoCenaViewModel.timer.start()

        JogoCenaViewModel.cronometroFinalizado.observe(this, {
            if (it == true) {
                encerraPartida()
            }
        })

        JogoCenaViewModel.animacaoResposta.observe(this, { resposta ->
            if (resposta == 1) {
                if (prefs.getBoolean("sons", true)) JogoCenaViewModel.tocarRespostaCorreta()
                executaAnimacao(animacaoRespostaCorreta)
            } else {
                if (prefs.getBoolean("sons", true)) JogoCenaViewModel.tocarRespostaErrada()
                executaAnimacao(animacaoRespostaErrada)
            }
        })

        JogoCenaViewModel.usuarioRecordeLiveData.observe(this, {
            JogoCenaViewModel.usuarioRecorde = it
        })

        JogoCenaViewModel.pontuacao.observe(
            this, { pontuacao ->
                binding.itemPontuacaoJogoCena.tvJogoCenaPontos.text = pontuacao.toString()
            }
        )

        JogoCenaViewModel.cronometro.observe(this, {
            binding.itemPontuacaoJogoCena.tvJogoCenaTempo.text = it.toString()
        })

        JogoCenaViewModel.listaAlternativas.observe(this, { listaAlternativas ->
            binding.includeJogoCenaBotoes.txtAlternativa1.text = listaAlternativas[0]
            binding.includeJogoCenaBotoes.txtAlternativa2.text = listaAlternativas[1]
            binding.includeJogoCenaBotoes.txtAlternativa3.text = listaAlternativas[2]
            binding.includeJogoCenaBotoes.txtAlternativa4.text = listaAlternativas[3]
        })

        novaRodada()
    }

    fun carregaCena() {
        Picasso.get().load("https://image.tmdb.org/t/p/w500" + JogoCenaViewModel.gerarCenas())
            .into(binding.itemCardCena.imgFilmeCena)
    }

    fun selecaoAlternativa(botaoPressionado: String) {

        if (JogoCenaViewModel.contadorFilme >= (JogoCenaViewModel.filmes.size - 1)) {

            binding.includeJogoCenaBotoes.imageButtonAlternativas1.isClickable = false
            binding.includeJogoCenaBotoes.imageButtonAlternativas2.isClickable = false
            binding.includeJogoCenaBotoes.imageButtonAlternativas3.isClickable = false
            binding.includeJogoCenaBotoes.imageButtonAlternativas4.isClickable = false

            encerraPartida()

        }else{
            JogoCenaViewModel.incrementaFilme()
            JogoCenaViewModel.resultadoResposta(botaoPressionado)
            novaRodada()
        }
    }

    fun encerraPartida() {

        if (prefs.getBoolean("musica", true)) {
            JogoCenaViewModel.pararMusica()
        }

        val resultadoDialog =
            ResultadoDialogAdapter(
                JogoCenaViewModel.pontuacao.value!!,
                "cena",
                Parametros.ID_JOGO_CENA,
                prefs
            )
        resultadoDialog.isCancelable = false
        resultadoDialog.show(supportFragmentManager, "resultadoDialog")
        JogoCenaViewModel.update()
    }

    fun novaRodada() {
        carregaCena()
        JogoCenaViewModel.gerarAlternativas()
    }

    fun inicializaAnimacao(animacao: LottieAnimationView, codAnimacao: Int) {

        animacao.addAnimatorListener(object : Animator.AnimatorListener {

            override fun onAnimationStart(animation: Animator?) {
                Log.i("ANIMATION", "INICIANDO ANIMACAO")
            }

            override fun onAnimationRepeat(animation: Animator?) {
                Log.i("ANIMATION", "REPETINDO ANIMACAO")
            }

            override fun onAnimationEnd(animation: Animator?) {
                Log.i("ANIMATION", "FINALIZANDO ANIMACAO")
                animacao.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator?) {
                Log.i("ANIMATION", "CANCELANDO ANIMACAO")
            }
        })

        if (codAnimacao == 1) {
            animacao.setAnimation(R.raw.correct_animation)
        } else {
            animacao.setAnimation(R.raw.wrong_animation)
        }
        animacao.speed = 1.8F
    }

    fun executaAnimacao(animacao: LottieAnimationView) {
        animacao.visibility = View.VISIBLE
        animacao.playAnimation()
    }

    fun vibrarBotão() {
        val vibrator = this.getSystemService(VIBRATOR_SERVICE) as Vibrator

        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(150)
        }
    }

    override fun onBackPressed() {

        val cancelaJogoDialog = CancelaJogoDialogAdapter(JogoCenaViewModel.musicaJogoCena)
        cancelaJogoDialog.isCancelable = false
        cancelaJogoDialog.show(supportFragmentManager, "cancelaJogoDialog")
    }

}