package br.com.cinequiz.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import br.com.cinequiz.R
import br.com.cinequiz.databinding.ActivityMenuBinding
import br.com.cinequiz.domain.Parametros
import br.com.cinequiz.room.CinequizApplication
import com.google.firebase.auth.FirebaseAuth

class MenuActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: ActivityMenuBinding
    private lateinit var prefs: SharedPreferences
    private var somItemSelecionado = MediaPlayer()

    private val menuViewModel: MenuViewModel by viewModels {
        MenuViewModelFactory(
            (application as CinequizApplication).repositoryUsuario,
            (application as CinequizApplication).repositoryMedalha,
            (application as CinequizApplication).repositoryUsuarioMedalha,
            (application as CinequizApplication).repositoryUsuarioRecorde,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        var usuarioId = mAuth.currentUser!!.uid
        var usuarioNome = mAuth.currentUser!!.displayName

        prefs = getSharedPreferences("userPrefs_$usuarioId", MODE_PRIVATE)
        inicializaAudio()

        menuViewModel.inicializaUsuario(
            getSharedPreferences("userPrefs_$usuarioId", MODE_PRIVATE),
            usuarioId,
            usuarioNome.toString()
        )

        binding.btnMenuDicas.btnItemDica.setOnClickListener {

            if (!isNetworkConnected()) {
                alertaSemConexao()
                return@setOnClickListener
            } else {
                executaSomItemMenu()
                vibrarBotão()
                val intent = Intent(this, LoadingActivity::class.java)
                intent.putExtra(Parametros.CHAVE_JOGO, Parametros.ID_JOGO_DICA)
                intent.putExtra(
                    Parametros.CHAVE_QUANTIDADE_FILMES,
                    Parametros.QUANTIDADE_INICIAL_FILMES_DICA
                )
                startActivity(intent)
            }
        }

        binding.btnMenuCenas.btnItemCena.setOnClickListener {

            if (!isNetworkConnected()) {
                alertaSemConexao()
                return@setOnClickListener
            } else {
                executaSomItemMenu()
                vibrarBotão()
                val intent = Intent(this, LoadingActivity::class.java)
                intent.putExtra(Parametros.CHAVE_JOGO, Parametros.ID_JOGO_CENA)
                intent.putExtra(
                    Parametros.CHAVE_QUANTIDADE_FILMES,
                    Parametros.QUANTIDADE_INICIAL_FILMES_CENA
                )
                startActivity(intent)
            }
        }

        binding.btnMenuMedalhas.setOnClickListener {
            if (prefs.getBoolean("sons", true)) somItemSelecionado.start()
            startActivity(Intent(this, MedalhasActivity::class.java))
        }

        binding.btnMenuOpcoes.setOnClickListener {
            if (prefs.getBoolean("sons", true)) somItemSelecionado.start()
            startActivity(Intent(this, OpcoesActivity::class.java))
        }
    }

    override fun onDestroy() {
        liberaAudio()
        super.onDestroy()
    }

    fun inicializaAudio() {
        somItemSelecionado = MediaPlayer.create(this, R.raw.item_menu_som)
    }

    fun liberaAudio() {
        somItemSelecionado.release()
    }

    fun executaSomItemMenu() {
        if (prefs.getBoolean("sons", true)) {
            somItemSelecionado.setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
                override fun onCompletion(p0: MediaPlayer?) {
                    somItemSelecionado.release()
                }
            })
            somItemSelecionado.start()
        }
    }

    fun vibrarBotão() {
        val vibrator = this.getSystemService(VIBRATOR_SERVICE) as Vibrator

        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(150)
        }
    }

    private fun alertaSemConexao() {
        AlertDialog.Builder(this).setTitle("Sem conexão")
            .setMessage("Ops, parece que voce não está conectado a internet. Verifique e tente novamente")
            .setPositiveButton(android.R.string.ok) { _, _ -> }
            .setIcon(android.R.drawable.ic_dialog_alert).show()
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager //1
        val networkInfo = connectivityManager.activeNetworkInfo //2
        return networkInfo != null && networkInfo.isConnected //3
    }
}
