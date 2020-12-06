package br.com.cinequiz.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.cinequiz.R
import br.com.cinequiz.domain.Filme
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

        viewModel.listaFilmesPopulares.observe(this) {
            for (filme in it) {
                viewModel.getFilme(filme.id)
            }
        }

        viewModel.filme.observe(this) {
            viewModel.listaFilmesUtilizaveis.add(it)

            Log.i("FilmeObserve", it.toString())

            if (ehFilmeUtilizavel(it)) {
                Log.i("MENUACTIVITY", "FOI")

            } else {
                Log.i("MENUACTIVITY", "NUMFOI")
            }
        }

        btnMenuDicas.btnItemDica.setOnClickListener {
            startActivity(Intent(this, JogoDica::class.java))
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


    fun ehFilmeUtilizavel(filme: Filme): Boolean {
    TODO("IDENTIFICAR QUANDO FILME EH INVALIDO PARA USO")
//        return when {
//            filme.equals(null) -> false
//            filme.imagensFilme.equals(null)  -> false
//            filme.imagensFilme[0].equals(null) -> false
//            filme.imagensFilme[0].file_path.equals(null) -> false
//            filme.filmesSimilares.equals(null) -> false
//            filme.filmesSimilares[0].equals(null) -> false
//            filme.filmesSimilares[0].equals(null) -> false
//            else -> true
//        }


        return true
    }
}
