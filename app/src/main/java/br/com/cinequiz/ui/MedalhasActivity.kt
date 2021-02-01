package br.com.cinequiz.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.cinequiz.adapters.MedalhasAdapter
import br.com.cinequiz.databinding.ActivityMedalhasBinding
import br.com.cinequiz.domain.UsuarioMedalhaJoin
import br.com.cinequiz.room.CinequizApplication
import com.google.firebase.auth.FirebaseAuth

class MedalhasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedalhasBinding

    private val adapter = MedalhasAdapter(this)

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

        val idUsuario = FirebaseAuth.getInstance().currentUser!!.uid

        binding.rvMedalhas.adapter = adapter

        binding.toolbarMedalhas.toolbarMain.setNavigationOnClickListener {
            onBackPressed()
        }

        medalhaViewModel.selecionaRecordeUsuario(idUsuario).observe(this, {
            binding.tvPontosCenas.text = it.popcornsCena.toString()
            binding.tvPontosDicas.text = it.popcornsDica.toString()
        })

        medalhaViewModel.selecionaMedalhasPossiveis(idUsuario).observe(this, Observer {
            it.forEach { medalha ->
                adapter.listaMedalhas.add(medalha)
                adapter.notifyItemInserted(adapter.listaMedalhas.lastIndex)
            }
        })

    }
}
