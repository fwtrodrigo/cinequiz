package br.com.cinequiz.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import br.com.cinequiz.R
import br.com.cinequiz.databinding.ActivityCadastroBinding
import com.google.firebase.auth.FirebaseAuth

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarCadastro.toolbarMain.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnCadastroCadastrar.setOnClickListener {
            val nome = binding.edtCadastroNome.text.toString()
            val email = binding.edtCadastroEmail.text.toString()
            val senha = binding.edtCadastroSenha.text.toString()
            val confirmacao = binding.edtCadastroConfirmacaoSenha.text.toString()

            if (validaFormulario(nome, email, senha, confirmacao)) {
                criaUsuario(email, senha)
            }
        }

        binding.edtCadastroNome.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                view.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.shape_anel_selecionado
                )
            } else {
                view.background = ContextCompat.getDrawable(this, R.drawable.shape_anel)
            }
        }

        binding.edtCadastroEmail.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                view.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.shape_anel_selecionado
                )
            } else {
                view.background = ContextCompat.getDrawable(this, R.drawable.shape_anel)
            }
        }

        binding.edtCadastroSenha.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                view.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.shape_anel_selecionado
                )
            } else {
                view.background = ContextCompat.getDrawable(this, R.drawable.shape_anel)
            }
        }

        binding.edtCadastroConfirmacaoSenha.setOnFocusChangeListener { view, hasFocus ->
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

    private fun criaUsuario(email: String, senha: String) {
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast("Conta criada com sucesso!")
                    voltaPaginaLogin()
                }
            }.addOnFailureListener {
                showToast(
                    "Erro ao cadastrar usuário. ${
                        it.message
                    }"
                )
            }
    }

    private fun validaFormulario(
        nome: String,
        email: String,
        senha: String,
        confirmacao: String
    ): Boolean {
        return when {
            nome.isEmpty() -> {
                binding.edtCadastroNome.requestFocus()
                binding.edtCadastroNome.error = "Obrigatório"
                showToast("Favor, preencher o campo de nome")
                false
            }

            email.isEmpty() -> {
                binding.edtCadastroEmail.requestFocus()
                binding.edtCadastroEmail.error = "Obrigatório"
                showToast("Favor, preencher o campo de e-mail")
                false
            }

            senha.isEmpty() -> {
                binding.edtCadastroSenha.requestFocus()
                binding.edtCadastroSenha.error = "Obrigatório"
                showToast("Favor, preencher o campo de senha")
                false
            }

            confirmacao.isEmpty() -> {
                binding.edtCadastroConfirmacaoSenha.requestFocus()
                binding.edtCadastroConfirmacaoSenha.error = "Obrigatório"
                showToast("Favor, preencher o campo de confirmação de senha")
                false
            }

            senha != confirmacao -> {
                showToast("A senha e a confirmação não são iguais")
                false
            }
            else -> {
                binding.edtCadastroNome.error = null
                binding.edtCadastroEmail.error = null
                binding.edtCadastroSenha.error = null
                binding.edtCadastroConfirmacaoSenha.error = null
                true
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

    private fun voltaPaginaLogin() {
        finish()
    }
}

