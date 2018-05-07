package lee.jaebeom.qrcodereader.main

import android.content.SharedPreferences
import io.reactivex.Observable
import lee.jaebeom.qrcodereader.History
import org.jsoup.nodes.Document

/**
 * Created by leejaebeom on 2018. 2. 18..
 */
interface MainContract {
    interface View{
        fun extractTitle(URL: String) : Observable<Document>
        fun intentScanner()
    }

    interface Presenter{
        fun attachView(view: View)
        fun detachView()
        fun extractData(URL: String) : Observable<Document>?
        fun addHistory()
        fun savePreference(pref:SharedPreferences, histories: ArrayList<History>)
//        fun loadPreference(pref: SharedPreferences) : String
        fun loadList(pref: SharedPreferences) : ArrayList<History>
    }
}