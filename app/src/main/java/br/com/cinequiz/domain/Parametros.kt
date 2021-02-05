package br.com.cinequiz.domain

class Parametros {
    companion object Jogos {
        //ID's
        val ID_JOGO_CENA = 1
        val ID_JOGO_DICA = 0
        val ID_RESPOSTA_CORRETA = 1
        val ID_RESPOSTA_ERRADA = 0

        //Jogos
        val CHAVE_JOGO = "id_jogo"
        val CHAVE_QUANTIDADE_FILMES = "quantidade_filmes"
        val CHAVE_LISTA_FILMES = "lista_filmes"

        //JogoCena
        val TEMPO_JOGO_CENA:Long = 60000 //60 Segundos
        val CONTADOR_JOGO_CENA = 60
        val PONTUACAO_ACERTO_JOGO_CENA = 10
        val QUANTIDADE_INICIAL_FILMES_CENA = 10

        //JogoDica
        val PONTUACAO_INICIAL_JOGO_DICA = 100
        val QUANTIDADE_INICIAL_FILMES_DICA = 10
        val PONTUACAO_ACERTO_JOGO_DICA = 40
        val PONTUACAO_ERRO_JOGO_DICA = 10
        val PONTUACAO_PROXIMA_DICA_JOGO_DICA = 10

    }
}
