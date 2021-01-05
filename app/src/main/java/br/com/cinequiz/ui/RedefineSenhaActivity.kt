package br.com.cinequiz.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import br.com.cinequiz.R
import br.com.cinequiz.databinding.ActivityRedefineSenhaBinding
import br.com.cinequiz.utils.ehEmailValido
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RedefineSenhaActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRedefineSenhaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRedefineSenhaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarRedefineSenha.toolbarMain.setNavigationOnClickListener{
            onBackPressed()
        }

        val emailAddress = binding.edtRedefineSenhaEmail.text.toString()

        Log.d("Redefine Senha", "$emailAddress isso que ta vindo do layout")
        //val emailAddress = "colocar de forma manual"

        binding.btnRedefineSenha.setOnClickListener {
            Log.d("REdefine Senha", "Antes de sendPasswordResetEmail $emailAddress")
            Firebase.auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener { task ->
                    Log.d("REdefine Senha", "Dentro de sendPasswordResetEmail")
                    if (task.isSuccessful) {
                        showToast("Email Enviado!")
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                    else {
                        showToast("Erro ${task.exception}")
                    }
                }
        }

    }

    private fun showToast(msg: String) {
        Toast.makeText(
            this,
            msg,
            Toast.LENGTH_SHORT
        ).show()
    }
}