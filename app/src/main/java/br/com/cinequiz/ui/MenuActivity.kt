package br.com.cinequiz.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.cinequiz.R
import br.com.cinequiz.service.repository
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.item_botao_selecao_modo_cena.view.*
import kotlinx.android.synthetic.main.item_botao_selecao_modo_dicas.view.*
import java.io.Serializable

class MenuActivity : AppCompatActivity() {

    val viewModel by viewModels<MenuViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MenuViewModel(repository) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        viewModel.getResults()

        viewModel.listaFilmesVotados.observe(this) {
            for (filme in it) {
                viewModel.getFilme(filme.id)
                Log.i("listaFilmesVotados", filme.id.toString() )
            }
        }

        btnMenuDicas.btnItemDica.setOnClickListener {

            val intent: Intent = Intent(this, JogoDica::class.java)
                .putExtra("listaFilmes", viewModel.listaFilmesUtilizaveis as Serializable)

            startActivity(intent)
        }

        btnMenuCenas.btnItemCena.setOnClickListener {

            val intent: Intent = Intent(this, JogoCena::class.java)
                .putExtra("listaFilmes", viewModel.listaFilmesUtilizaveis as Serializable)

            startActivity(intent)
        }

        btnMenuMedalhas.setOnClickListener {
            startActivity(Intent(this, MedalhasActivity::class.java))
        }

        btnMenuOpcoes.setOnClickListener {
            startActivity(Intent(this, OpcoesActivity::class.java))
        }
    }
}
