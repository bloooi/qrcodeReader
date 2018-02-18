package lee.jaebeom.qrcodereader.main

import io.reactivex.Observable
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
        fun detacthView()
        fun extractData(URL: String) : Observable<Document>?
        fun addHistory()
    }
}