package br.com.cinequiz.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.cinequiz.R
import br.com.cinequiz.databinding.ActivitySplashScreenBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    val scope = CoroutineScope(Dispatchers.Main)
    private lateinit var  mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        scope.launch {
            delay(3000)
            if(user != null){
                val intent = Intent(application, MenuActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(application, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}


