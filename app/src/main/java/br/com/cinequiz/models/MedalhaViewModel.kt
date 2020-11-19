package br.com.cinequiz.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.cinequiz.domain.Medalha
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MedalhaViewModel (application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

    fun getListMedalhasGson(): ArrayList<Medalha> {

        val jsonString = context.assets.open("medalhas.json")
            .bufferedReader()
            .use { it.readText() }

        return Gson().fromJson(jsonString, object : TypeToken<ArrayList<Medalha>>(){}.type)
    }
}