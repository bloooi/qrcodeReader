package lee.jaebeom.qrcodereader.main

import io.reactivex.Observable
import org.jsoup.nodes.Document

/**
 * Created by leejaebeom on 2018. 2. 18..
 */
class MainPresenter : MainContract.Presenter {
    var view: MainContract.View? = null

    override fun attachView(view: MainContract.View) {
       this.view = view
    }

    override fun detacthView() {
        view = null
    }
    override fun extractData(URL: String) : Observable<Document>? {
        return view?.extractTitle(URL)
    }

    override fun addHistory() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}