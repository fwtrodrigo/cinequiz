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

        viewModel.listaFilmesPopulares.observe(this) {
            for (filme in it) {
                Log.i("FILME ID:", filme.id.toString())

                viewModel.getFilme(filme.id)
            }
        }

        viewModel.filme.observe(this) {
            Log.i("FILME TITULO:", it.title)
        }

        btnMenuDicas.btnItemDica.setOnClickListener {
            startActivity(Intent(this, JogoDica::class.java))
        }

        btnMenuCenas.btnItemCena.setOnClickListener {
            startActivity(Intent(this, JogoCena::class.java))
        }

        btnMenuMedalhas.setOnClickListener {
            startActivity(Intent(this, MedalhasActivity::class.java))
        }

        btnMenuOpcoes.setOnClickListener {
            startActivity(Intent(this, OpcoesActivity::class.java))
        }
    }
}