package lee.jaebeom.qrcodereader.main

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import lee.jaebeom.qrcodereader.History
import lee.jaebeom.qrcodereader.util.SavePreference
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val gson = Gson()
    var histories = MutableLiveData<List<History>>()
    init {
        histories.value = loadList()
    }
    fun savePreference(histories: ArrayList<History>)
            = SavePreference.saveSharedPreference(getApplication(), "histories", gson.toJson(histories))

    private fun loadPreference(): String
            = SavePreference.getStringPreference(getApplication(), "histories")

    private fun loadList(): List<History>? {
        val type = object : TypeToken<List<History>>(){}.type
        return gson.fromJson(loadPreference(), type)
    }

    fun extractData(URL: String) : Observable<Document>? {
       return Observable.create<Document> {subscriber ->
            val doc = Jsoup.connect(URL).get()
            subscriber.onNext(doc)
        }
    }
}