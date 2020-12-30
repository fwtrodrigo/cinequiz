package br.com.cinequiz.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.cinequiz.R
import br.com.cinequiz.databinding.ActivityMenuBinding
import br.com.cinequiz.service.repository
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.item_botao_selecao_modo_cena.view.*
import kotlinx.android.synthetic.main.item_botao_selecao_modo_dicas.view.*
import java.io.Serializable

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    val viewModel by viewModels<MenuViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MenuViewModel(repository) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getResults()

        viewModel.listaFilmesVotados.observe(this) {
            for (filme in it) {
                viewModel.getFilme(filme.id)
                Log.i("listaFilmesVotados", filme.id.toString() )
            }
        }

        binding.btnMenuDicas.btnItemDica.setOnClickListener {

            val intent: Intent = Intent(this, JogoDica::class.java)
                .putExtra("listaFilmes", viewModel.listaFilmesUtilizaveis as Serializable)

            startActivity(intent)
        }

        binding.btnMenuCenas.btnItemCena.setOnClickListener {

            val intent: Intent = Intent(this, JogoCena::class.java)
                .putExtra("listaFilmes", viewModel.listaFilmesUtilizaveis as Serializable)

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
