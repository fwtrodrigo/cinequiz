package br.com.cinequiz.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import br.com.cinequiz.R
import br.com.cinequiz.databinding.ActivityLoginBinding
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*

//851294246873-0gel4t589odrfmqa2r5vl0si8n7i68f7.apps.googleusercontent.com

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"
    private lateinit var mAuth: FirebaseAuth
    private var RC_SIGN_IN = 100
    private lateinit var binding: ActivityLoginBinding

    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controlaFocoEditText()

        mAuth = FirebaseAuth.getInstance()

        callbackManager = CallbackManager.Factory.create()

        binding.facebookSignInButton.setOnClickListener {
            callFacebookRegisterCallback()
            LoginManager.getInstance()
                .logInWithReadPermissions(this, listOf("public_profile", "email", "user_friends"))
        }

        binding.googleSignInButton.setOnClickListener() {
            googleSignIn()
        }

        binding.btnLoginCadastrar.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }

        binding.btnLoginEntrar.setOnClickListener {
            signinWithEmail()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)


        showToast("onActivityResult:   " + data.toString())

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                val account = task.getResult(ApiException::class.java)!!
//                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
//                firebaseAuthWithGoogle(account.idToken!!)
//            } catch (e: ApiException) {
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e)
//                // ...
//            }
//        }
    }

    private fun googleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "GOOGLEsignInWithCredential:success")
                    appLogin()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "GOOGLEsignInWithCredential:failure", task.exception)
                }
            }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "FACEBOOKsignInWithCredential:success")
                    appLogin()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "FACEBOOKsignInWithCredential:failure", task.exception)
                }
            }
    }

    private fun appLogin() {
        startActivity(Intent(this, MenuActivity::class.java))
        finish()
    }

    private fun signinWithEmail() {
        val email = binding.edtLoginEmail.text.toString()
        val senha = binding.edtLoginSenha.text.toString()

        if (!validaFormulario(email, senha)) {
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast("Bem vindo, UserID: ${it.result?.user?.uid}")
                    appLogin()
                }
            }.addOnFailureListener {
                showToast(it.message.toString())
            }
    }

    private fun showToast(msg: String) {
        Toast.makeText(
            this,
            msg,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun validaFormulario(email: String, senha: String): Boolean {
        return when {
            email.isEmpty() -> {
                binding.edtLoginEmail.requestFocus()
                binding.edtLoginEmail.error = "Obrigatório"
                showToast("Favor, preencher o campo de e-mail")
                false
            }

            senha.isEmpty() -> {
                binding.edtLoginSenha.requestFocus()
                binding.edtLoginSenha.error = "Obrigatório"
                showToast("Favor, preencher o campo de senha")
                false
            }
            else -> {
                binding.edtLoginEmail.error = null
                binding.edtLoginSenha.error = null
                true
            }
        }
    }

    private fun callFacebookRegisterCallback() {
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    Log.d("TAG", "Success Login")
                }

                override fun onCancel() {
                    Toast.makeText(this@LoginActivity, "Login Cancelled", Toast.LENGTH_LONG).show()
                }

                override fun onError(exception: FacebookException) {
                    Toast.makeText(this@LoginActivity, "exception.message", Toast.LENGTH_LONG)
                        .show()
                }
            })
    }

    private fun controlaFocoEditText() {
        binding.edtLoginEmail.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                view.background = ContextCompat.getDrawable(this, R.drawable.shape_anel_selecionado)
            } else {
                view.background = ContextCompat.getDrawable(this, R.drawable.shape_anel)
            }
        }

        binding.edtLoginSenha.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                view.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.shape_anel_selecionado
                )
            } else {
                view.background = ContextCompat.getDrawable(this, R.drawable.shape_anel)
            }
        }
    }
}
