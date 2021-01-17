package br.com.cinequiz.ui


import android.app.Application
import android.view.Gravity
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.cinequiz.domain.Filme


class JogoDicaViewModel(application: Application): AndroidViewModel(application)  {
    private val context = getApplication<Application>().applicationContext

    val pontuacao = MutableLiveData<Int>(150)

    var listaDicas = MutableLiveData<ArrayList<String>>()
    var listaDicasGeradas = ArrayList<String>()
    var contadorDica = 0

    var listaAlternativas = MutableLiveData<ArrayList<String>>()
    var alternativaCorreta = ""

    var filmes = arrayListOf<Filme>()
    var contadorFilme = 0


    fun gerarDicas(){

        contadorDica = 0

        listaDicas.value = arrayListOf()
        listaDicasGeradas = arrayListOf(
            "${filmes[contadorFilme].pessoasFilme[0].name} atuou como um dos meus personagens.",
            "Fui produzido pelo estúdio ${filmes[contadorFilme].production_companies[0].name}.",
            "Minha estreia foi em ${filmes[contadorFilme].formataDataLancamento()}.",
            "Um dos meus personagens se chama ${filmes[contadorFilme].pessoasFilme[0].character}."
        )

        listaDicasGeradas.shuffle()
        for (i in 0 until listaDicasGeradas.size){
            listaDicasGeradas[i] = (i+1).toString() + " - " + listaDicasGeradas[i]
        }

        proximaDica(0)
    }

    fun gerarAlternativas(){
        var alternativasGeradas = arrayListOf(
            filmes[contadorFilme].title,
            filmes[contadorFilme].filmesSimilares[0].title,
            filmes[contadorFilme].filmesSimilares[1].title,
            filmes[contadorFilme].filmesSimilares[2].title
        )

        alternativasGeradas.shuffle()
        listaAlternativas.value = alternativasGeradas

        alternativasGeradas.forEachIndexed { index, alternativa ->
            if(alternativa == filmes[contadorFilme].title) alternativaCorreta = "btn"+(index+1)
        }
    }

    fun proximaDica(pontosDescontados: Int) {

        if(contadorDica == listaDicasGeradas.size){
            val toast = Toast.makeText(context, "Sem dicas disponíveis", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP, 0, 450)
            toast.show()
        }else{
            var listaAuxiliar = listaDicas.value
            listaAuxiliar!!.add((listaDicasGeradas.get(contadorDica)))
            listaDicas.value = listaAuxiliar
            contadorDica++
            descontaPontuacao(pontosDescontados)
        }
    }

    fun resultadoResposta(resposta: String){
        if(resposta == alternativaCorreta){
            Toast.makeText(context, "Acertou", Toast.LENGTH_SHORT).show()
            aumentaPontuacao(40)
        }else{
            Toast.makeText(context, "Errou", Toast.LENGTH_SHORT).show()
        }
    }

    fun aumentaPontuacao(pontosGanhos: Int){
        pontuacao.value = pontuacao.value?.plus(pontosGanhos)
    }

    fun descontaPontuacao(pontosDescontados: Int){
        pontuacao.value = pontuacao.value?.minus(pontosDescontados)
    }

    fun incrementaFilme(){
        contadorFilme++
    }

}
