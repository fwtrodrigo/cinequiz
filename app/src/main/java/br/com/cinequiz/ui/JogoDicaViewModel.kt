package br.com.cinequiz.ui


import android.app.Application
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.lifecycle.*
import br.com.cinequiz.domain.Filme
import br.com.cinequiz.domain.UsuarioRecorde
import br.com.cinequiz.room.repository.UsuarioRecordeRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class JogoDicaViewModel(
    application: Application,
    private val usuarioRecordeRepository: UsuarioRecordeRepository
) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

    var usuarioId = FirebaseAuth.getInstance().currentUser!!.uid
    var usuarioRecordeLiveData: LiveData<UsuarioRecorde> = usuarioRecordeRepository.get(usuarioId)

    fun update() = viewModelScope.launch {
        val p = usuarioRecorde.popcornsDica
        Log.i("JOGODICAVIEWMODEL", usuarioRecorde.toString())
        if (p < pontuacao.value!!) {
            usuarioRecordeRepository.atualizaPontuacaoDica(usuarioId, pontuacao.value!!)
        }
    }

    lateinit var usuarioRecorde : UsuarioRecorde
    val pontuacao = MutableLiveData<Int>(150)

    var listaDicas = MutableLiveData<ArrayList<String>>()
    var listaDicasGeradas = ArrayList<String>()
    var contadorDica = 0

    var listaAlternativas = MutableLiveData<ArrayList<String>>()
    var alternativaCorreta = ""

    var filmes = arrayListOf<Filme>()
    var contadorFilme = 0


    fun gerarDicas() {

        contadorDica = 0

        listaDicas.value = arrayListOf()
        listaDicasGeradas = arrayListOf(
            "${filmes[contadorFilme].pessoasFilme[0].name} atuou como um dos meus personagens.",
            "Fui produzido pelo estúdio ${filmes[contadorFilme].production_companies[0].name}.",
            "Minha estreia foi em ${filmes[contadorFilme].formataDataLancamento()}.",
            "Um dos meus personagens se chama ${filmes[contadorFilme].pessoasFilme[0].character}."
        )

        listaDicasGeradas.shuffle()
        for (i in 0 until listaDicasGeradas.size) {
            listaDicasGeradas[i] = (i + 1).toString() + " - " + listaDicasGeradas[i]
        }

        proximaDica(0)
    }

    fun gerarAlternativas() {
        var alternativasGeradas = arrayListOf(
            filmes[contadorFilme].title,
            filmes[contadorFilme].filmesSimilares[0].title,
            filmes[contadorFilme].filmesSimilares[1].title,
            filmes[contadorFilme].filmesSimilares[2].title
        )

        alternativasGeradas.shuffle()
        listaAlternativas.value = alternativasGeradas

        alternativasGeradas.forEachIndexed { index, alternativa ->
            if (alternativa == filmes[contadorFilme].title) alternativaCorreta = "btn" + (index + 1)
        }
    }

    fun proximaDica(pontosDescontados: Int) {

        if (contadorDica == listaDicasGeradas.size) {
            val toast = Toast.makeText(context, "Sem dicas disponíveis", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP, 0, 450)
            toast.show()
        } else {
            var listaAuxiliar = listaDicas.value
            listaAuxiliar!!.add((listaDicasGeradas.get(contadorDica)))
            listaDicas.value = listaAuxiliar
            contadorDica++
            descontaPontuacao(pontosDescontados)
        }
    }

    fun resultadoResposta(resposta: String) {
        if (resposta == alternativaCorreta) {
            Toast.makeText(context, "Acertou", Toast.LENGTH_SHORT).show()
            aumentaPontuacao(40)
        } else {
            Toast.makeText(context, "Errou", Toast.LENGTH_SHORT).show()
        }
    }

    fun aumentaPontuacao(pontosGanhos: Int) {
        pontuacao.value = pontuacao.value?.plus(pontosGanhos)
    }

    fun descontaPontuacao(pontosDescontados: Int) {
        pontuacao.value = pontuacao.value?.minus(pontosDescontados)
    }

    fun incrementaFilme() {
        contadorFilme++
    }

}

class JogoDicaViewModelFactory(
    val application: Application,
    private val repository: UsuarioRecordeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JogoDicaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JogoDicaViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}