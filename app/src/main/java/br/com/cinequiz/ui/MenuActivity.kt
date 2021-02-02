package br.com.cinequiz.ui

import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
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
        somItemSelecionado = MediaPlayer.create(this, R.raw.item_menu_som)

        menuViewModel.inicializaUsuario(
            getSharedPreferences("userPrefs_$usuarioId", MODE_PRIVATE),
            usuarioId,
            usuarioNome.toString()
        )

        binding.btnMenuDicas.btnItemDica.setOnClickListener {
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

        binding.btnMenuCenas.btnItemCena.setOnClickListener {
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

        binding.btnMenuMedalhas.setOnClickListener {
            executaSomItemMenu()
            startActivity(Intent(this, MedalhasActivity::class.java))
        }

        binding.btnMenuOpcoes.setOnClickListener {
            executaSomItemMenu()
            startActivity(Intent(this, OpcoesActivity::class.java))
        }
    }

    fun executaSomItemMenu(){
        if(prefs.getBoolean("sons", true)) somItemSelecionado.start()
    }

    fun vibrarBotão() {
        val vibrator = this.getSystemService(VIBRATOR_SERVICE) as Vibrator

        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(150)
        }
    }

}
