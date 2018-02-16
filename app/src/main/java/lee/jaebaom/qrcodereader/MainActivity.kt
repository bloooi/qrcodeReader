package lee.jaebaom.qrcodereader

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jsoup.Jsoup
import java.util.*

class MainActivity : AppCompatActivity() {

    private val histories = ArrayList<History>()
    private val adapter = MainRecyclerAdapter(histories)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        recycler.adapter = adapter

        fab.setOnClickListener { view ->
            val intent = IntentIntegrator(this)
            intent.setOrientationLocked(false)
            intent.initiateScan()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val intentResult: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        extractTitle(intentResult?.contents!!)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.newThread())
                .map{
                    onNext ->
                        histories.add(History(onNext, intentResult.contents))
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {

                    adapter.notifyDataSetChanged()
                }


        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun extractTitle(URL: String) : Observable<String>{
        return Observable.create<String> {subscriber ->
            val doc = Jsoup.connect(URL).get()
            subscriber.onNext(doc.title())
        }

    }
}
