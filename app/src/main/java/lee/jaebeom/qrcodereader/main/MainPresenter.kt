package lee.jaebeom.qrcodereader.main

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import lee.jaebeom.qrcodereader.History
import lee.jaebeom.qrcodereader.util.SavePreference
import org.jsoup.nodes.Document

/**
 * Created by leejaebeom on 2018. 2. 18..
 */
class MainPresenter : MainContract.Presenter {
    var view: MainContract.View? = null
    private val gson = Gson()

    override fun attachView(view: MainContract.View) {
       this.view = view
    }

    override fun detachView() {
        view = null
    }
    override fun extractData(URL: String) : Observable<Document>? {
        return view?.extractTitle(URL)
    }

    override fun addHistory() {
    }

    override fun savePreference(pref: SharedPreferences, histories: ArrayList<History>)
            = SavePreference(pref).saveSharedPreference("histories", gson.toJson(histories))

    private fun loadPreference(pref: SharedPreferences): String
            = SavePreference(pref).getStringPreference("histories")

    override fun loadList(pref: SharedPreferences): ArrayList<History>? {
        val type = object : TypeToken<List<History>>(){}.type
        return gson.fromJson(loadPreference(pref), type)
    }
}