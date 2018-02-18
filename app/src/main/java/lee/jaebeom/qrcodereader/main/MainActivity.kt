package lee.jaebeom.qrcodereader.main

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import lee.jaebeom.qrcodereader.History
import lee.jaebeom.qrcodereader.R
import lee.jaebeom.qrcodereader.ScanActivity
import lee.jaebeom.qrcodereader.WebActivity
import lee.jaebeom.qrcodereader.util.SavaPreference
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), MainContract.View {

    private var presenter: MainContract.Presenter? = null
    private var histories = ArrayList<History>()
    private lateinit var adapter : MainRecyclerAdapter
    private val gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        attachPresenter()

        SavaPreference.getStringPreference(this, "histories")

        initHistories()
        adapter = MainRecyclerAdapter(this, histories)
        recycler.adapter = adapter

        if(histories.isEmpty()){
            empty_view.visibility = View.VISIBLE
        }

        val divderDecoration = DividerItemDecoration(applicationContext, LinearLayoutManager(this).orientation)
        recycler.addItemDecoration(divderDecoration)

        fab.setOnClickListener {
            intentScanner()
        }
    }

    private fun attachPresenter(){
        presenter = lastCustomNonConfigurationInstance as MainContract.Presenter? ?: MainPresenter()
        presenter?.attachView(this)
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return presenter!!
    }

    override fun onDestroy() {
        presenter?.detacthView()
        super.onDestroy()
    }
    //메뉴를 딱히 안씀
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val intentResult: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        val intent = Intent(this, WebActivity::class.java)
        if (data != null){
            if (intentResult?.contents != null){
                progress.visibility = View.VISIBLE
                presenter?.extractData(intentResult.contents)
                        ?.subscribeOn(Schedulers.computation())
                        ?.observeOn(Schedulers.newThread())
                        ?.map{
                            onNext ->
                            val dateTimeFormat = SimpleDateFormat("MM-dd HH:mm", Locale.KOREA)
                            val cal = dateTimeFormat.format(Calendar.getInstance().time)
                            histories.add(0, History(onNext.title(), onNext.location(), cal))
                            SavaPreference.saveShaerdPreference(this, "histories", gson.toJson(histories))
                            intent.putExtra("url", onNext.location())
                            intent.putExtra("name", onNext.title())
                        }
                        ?.observeOn(AndroidSchedulers.mainThread())
                        ?.subscribe {
                            adapter.notifyDataSetChanged()
                            progress.visibility = View.GONE
                            empty_view.visibility = View.GONE
                            startActivity(intent)

                        }
            }else{
                Snackbar.make(root, "인식되지 않았습니다. 다시 시도해 주세요.", Snackbar.LENGTH_LONG).show()
            }
        }


        super.onActivityResult(requestCode, resultCode, data)
    }

    //view override function
    override fun extractTitle(URL: String) : Observable<Document>{
        return Observable.create<Document> {subscriber ->
            val doc = Jsoup.connect(URL).get()
            subscriber.onNext(doc)
        }

    }

    override fun intentScanner() {
        IntentIntegrator(this)
                .setOrientationLocked(false)
                .setCaptureActivity(ScanActivity::class.java)
                .setBeepEnabled(false)
                .initiateScan()
    }

    private fun initHistories(){
        val type = object : TypeToken<List<History>>(){}.type;
        histories = gson.fromJson(SavaPreference.getStringPreference(this, "histories"), type) ?: ArrayList<History>()
    }
}
