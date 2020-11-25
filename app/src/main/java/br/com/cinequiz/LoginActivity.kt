package br.com.cinequiz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edtLoginEmail.setOnFocusChangeListener { view, hasFocus ->
           if(hasFocus) {
               view.background =  ContextCompat.getDrawable(this, R.drawable.shape_anel_selecionado)
           } else {
               view.background =  ContextCompat.getDrawable(this, R.drawable.shape_anel)
           }
        }

        edtLoginSenha.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus) {
                view.background =  ContextCompat.getDrawable(
                    this,
                    R.drawable.shape_anel_selecionado
                )
            } else {
                view.background =  ContextCompat.getDrawable(this, R.drawable.shape_anel)
            }
        }


        btnLoginCadastrar.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }

        btnLoginEntrar.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            finish()
        }


    }
}
