package br.com.cinequiz.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.cinequiz.databinding.ActivityMedalhasBinding
import br.com.cinequiz.room.CinequizApplication

class MedalhasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedalhasBinding


    private val medalhaViewModel: MedalhaViewModel by viewModels {
        MedalhaViewModelFactory((application as CinequizApplication).repositoryUsuarioMedalha)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedalhasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMedalhas.toolbarMain.setNavigationOnClickListener {
            onBackPressed()
        }

        //val adapter = MedalhasAdapter(listaMedalhas)
        //binding.rvMedalhas.adapter = adapter

//        medalhaViewModel.selecionaMedalhasNaoConquistadas("HAL9000").observe(this, Observer {
//            it.forEach { Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show() }
//        })

        medalhaViewModel.selecionaMedalhasPossiveis("HAL9000").observe(this, Observer {
            it.forEach { Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show() }
        })

    }
}
