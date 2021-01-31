package br.com.cinequiz.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import br.com.cinequiz.databinding.ActivityMenuBinding
import br.com.cinequiz.room.CinequizApplication
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.item_botao_selecao_modo_cena.view.*
import kotlinx.android.synthetic.main.item_botao_selecao_modo_dicas.view.*
import java.io.Serializable

class MenuActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: ActivityMenuBinding

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
        menuViewModel.inicializaUsuario(getSharedPreferences("userPrefs_$usuarioId", MODE_PRIVATE), usuarioId, usuarioNome.toString())

        binding.btnMenuDicas.btnItemDica.setOnClickListener {

            val intent = Intent(this, LoadingActivity::class.java)
            startActivity(intent)
        }

        binding.btnMenuCenas.btnItemCena.setOnClickListener {

            val intent = Intent(this, LoadingActivity::class.java)
            startActivity(intent)
        }

        binding.btnMenuMedalhas.setOnClickListener {
            startActivity(Intent(this, MedalhasActivity::class.java))
        }

        binding.btnMenuOpcoes.setOnClickListener {
            startActivity(Intent(this, OpcoesActivity::class.java))
        }

    }
}
