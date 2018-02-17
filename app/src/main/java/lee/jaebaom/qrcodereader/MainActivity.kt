package lee.jaebaom.qrcodereader

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import lee.jaebaom.qrcodereader.util.SavaPreference
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private var histories = ArrayList<History>()
    private lateinit var adapter : MainRecyclerAdapter
    private val gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        SavaPreference.getStringPreference(this, "histories")

        initHistories()
        adapter = MainRecyclerAdapter(this, histories)
        recycler.adapter = adapter

        val divderDecoration = DividerItemDecoration(applicationContext, LinearLayoutManager(this).orientation)
        recycler.addItemDecoration(divderDecoration)

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
        val intent = Intent(this, WebActivity::class.java)
        if (data != null){
            extractTitle(intentResult?.contents!!)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(Schedulers.newThread())
                    .map{
                        onNext ->
                        val dateTimeFormat = SimpleDateFormat("MM-dd HH:mm", Locale.KOREA)
                        val cal = dateTimeFormat.format(Calendar.getInstance().time)
                        histories.add(0, History(onNext, intentResult.contents, cal))
                        SavaPreference.saveShaerdPreference(this, "histories", gson.toJson(histories))
                        intent.putExtra("url", intentResult.contents)
                        intent.putExtra("name", onNext)
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        adapter.notifyDataSetChanged()
                        startActivity(intent)

                    }
        }


        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun extractTitle(URL: String) : Observable<String>{
        return Observable.create<String> {subscriber ->
            val doc = Jsoup.connect(URL).get()
            subscriber.onNext(doc.title())
        }

    }

    private fun initHistories(){
        val type = object : TypeToken<List<History>>(){}.type;
        histories = gson.fromJson(SavaPreference.getStringPreference(this, "histories"), type) ?: ArrayList<History>()
    }
}
