package br.com.cinequiz.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import br.com.cinequiz.adapters.MedalhasAdapter
import br.com.cinequiz.databinding.ActivityMedalhasBinding
import br.com.cinequiz.domain.UsuarioMedalhaJoin
import br.com.cinequiz.room.CinequizApplication
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MedalhasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedalhasBinding

    private val medalhaViewModel: MedalhaViewModel by viewModels {
        MedalhaViewModelFactory(
            (application as CinequizApplication).repositoryUsuarioMedalha,
            (application as CinequizApplication).repositoryUsuarioRecorde
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedalhasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ctx = this

        val idUsuario = FirebaseAuth.getInstance().currentUser!!.uid

        binding.toolbarMedalhas.toolbarMain.setNavigationOnClickListener {
            onBackPressed()
        }

        medalhaViewModel.selecionaRecordeUsuario(idUsuario).observe(this, {
            binding.tvPontosCenas.text = it.popcornsCena.toString()
            binding.tvPontosDicas.text = it.popcornsDica.toString()
        })

//        medalhaViewModel.selecionaMedalhasPossiveis(idUsuario).observe(this, Observer {
//            it.forEach { medalha ->
//                adapter.listaMedalhas.add(medalha)
//                adapter.notifyItemInserted(adapter.listaMedalhas.lastIndex)
//            }
//        })

        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            val listaMedalhas = medalhaViewModel.selecionaMedalhasDisponiveis(idUsuario) as ArrayList<UsuarioMedalhaJoin>
            val adapter = MedalhasAdapter(ctx, listaMedalhas)
            binding.rvMedalhas.adapter = adapter

            adapter.notifyDataSetChanged()
        }

    }
}
