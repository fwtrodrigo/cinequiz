package br.com.cinequiz.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import br.com.cinequiz.R
import br.com.cinequiz.databinding.ActivityLoginBinding
import br.com.cinequiz.utils.ehEmailValido
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_login.*

//851294246873-0gel4t589odrfmqa2r5vl0si8n7i68f7.apps.googleusercontent.com

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private lateinit var callbackManager: CallbackManager

    private val TAG = "LoginActivity"

    private val GOOGLE_LOG_IN_RC = 1
    private val FACEBOOK_LOG_IN_RC = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controlaFocoEditText()

        mAuth = FirebaseAuth.getInstance()

        callbackManager = CallbackManager.Factory.create()

        binding.facebookSignInButton.setOnClickListener {
            LoginManager.getInstance()
                .logInWithReadPermissions(this, listOf("public_profile", "email"))

            callFacebookRegisterCallback()
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
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)

        showToast("onActivityResult:   $requestCode --- $resultCode")

        if (requestCode == GOOGLE_LOG_IN_RC) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            task.addOnCompleteListener {
                if (it.isSuccessful) {
                    val account = task.getResult(ApiException::class.java)!!
                    showToast("logou com google firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } else {
                    showToast("Google sign in failed " + it.exception)
                }
            }
        }
    }

    private fun googleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_LOG_IN_RC)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "GOOGLEsignInWithCredential:success")
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "GOOGLEsignInWithCredential:failure", task.exception)
                }
            }
    }

    private fun appLogin() {
        startActivity(Intent(this, MenuActivity::class.java))
        finish()
    }

    private fun callFacebookRegisterCallback() {
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    Toast.makeText(this@LoginActivity, "Login SUCCESS", Toast.LENGTH_LONG).show()
                    handleFacebookAccessToken(loginResult!!.accessToken)
                }

                override fun onCancel() {
                    Toast.makeText(this@LoginActivity, "Login Cancelled", Toast.LENGTH_LONG).show()
                }

                override fun onError(exception: FacebookException) {
                    Toast.makeText(
                        this@LoginActivity,
                        "deu ruim: " + exception.message.toString(),
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            })
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
                    Log.w(TAG, "erro FACEBOOKsignInWithCredential:failure", task.exception)
                }
            }
    }

    private fun signinWithEmail() {
        val email = binding.edtLoginEmail.text.toString()
        val senha = binding.edtLoginSenha.text.toString()

        if (!validaFormulario(email, senha)) {
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener {
                //conclusao request
            }.addOnSuccessListener {
                showToast("Bem vindo, UserID: ${it.user?.uid}")
                appLogin()

            }.addOnFailureListener {
                if (it is FirebaseAuthInvalidCredentialsException) {
                    showToast("Usuário e/ou senha inválidos.")
                }

                if (it is FirebaseAuthInvalidUserException) {
                    showToast("Usuário e/ou senha inválidos.")
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

    private fun validaFormulario(email: String, senha: String): Boolean {
        return when {
            email.isEmpty() -> {
                binding.edtLoginEmail.requestFocus()
                binding.edtLoginEmail.error = getString(R.string.obrigatorio)
                showToast(getString(R.string.preencher, "email"))
                false
            }

            !ehEmailValido(email) -> {
                binding.edtLoginEmail.requestFocus()
                binding.edtLoginEmail.error = getString(R.string.formato_email_invalido)
                showToast(getString(R.string.informe_email_valido))
                false
            }

            senha.isEmpty() -> {
                binding.edtLoginSenha.requestFocus()
                binding.edtLoginSenha.error = getString(R.string.obrigatorio)
                showToast(getString(R.string.preencher, "senha"))
                false
            }
            else -> {
                binding.edtLoginEmail.error = null
                binding.edtLoginSenha.error = null
                true
            }
        }
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
