package lee.jaebeom.qrcodereader.main

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
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
import lee.jaebeom.qrcodereader.util.Checker
import lee.jaebeom.qrcodereader.util.SavePreference
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), MainContract.View, MainCallback.OnItemHelperListener {
    private var presenter: MainContract.Presenter? = null
    private var histories = ArrayList<History>()
    private lateinit var adapter : MainRecyclerAdapter
    private val preference : SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        attachPresenter()
        initHistories()

        adapter = MainRecyclerAdapter(this, histories)
        recycler.adapter = adapter

//        val itemTouchHelper = ItemTouchHelper(MainCallback())
//        itemTouchHelper.attachToRecyclerView(recycler)

        if(histories.isEmpty()){
            empty_view.visibility = View.VISIBLE
        }

        recycler.addItemDecoration(DividerItemDecoration(applicationContext, LinearLayoutManager(this).orientation))

        ItemTouchHelper(MainCallback(0, ItemTouchHelper.LEFT, this)).attachToRecyclerView(recycler)
//        val simpleHelper: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.UP){
//            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean = false
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {}
//        }
//        ItemTouchHelper(simpleHelper).attachToRecyclerView(recycler)
//

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
        presenter?.detachView()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val intentResult: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        val intent = Intent(this, WebActivity::class.java)
        val cal = SimpleDateFormat("MM-dd HH:mm", Locale.KOREA).format(Calendar.getInstance().time)

        if (data != null){
            if (intentResult?.contents != null){
                progress.visibility = View.VISIBLE
                if (Checker.checkData(intentResult.contents) == "URL"){ //바코드 정보가 URL일 경우
                    presenter?.extractData(intentResult.contents)
                            ?.subscribeOn(Schedulers.computation())
                            ?.observeOn(Schedulers.newThread())
                            ?.map{
                                onNext ->
                                histories.add(0, History(onNext.title(), onNext.location(), cal))
                                presenter?.savePreference(preference, histories)
//                                SavePreference.saveSharedPreference(this, "histories", gson.toJson(histories))
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
                    histories.add(0, History(intentResult.contents, intentResult.contents, cal))
                    presenter?.savePreference(preference, histories)
//                    SavePreference.saveSharedPreference(this, "histories", gson.toJson(histories))
                    empty_view.visibility = View.GONE
                    progress.visibility = View.GONE
                    adapter.notifyDataSetChanged()
                    Snackbar.make(root, "저장됐어요!", Snackbar.LENGTH_SHORT).show()
                }



            }else{
                Snackbar.make(root, "인식되지 않았어요. 다시 시도해 주세요. ㅠㅠ", Snackbar.LENGTH_LONG).show()
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
        histories = presenter?.loadList(preference) ?: ArrayList()
//        histories = gson.fromJson(SavePreference.getStringPreference(this, "histories"), type) ?: ArrayList<History>()
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int) {
        if (viewHolder is MainRecyclerAdapter.MainViewHolder){
            val index = viewHolder.adapterPosition
            val item = histories[index]

            adapter.removeItem(index)
            presenter?.savePreference(preference, histories)
            Snackbar.make(viewHolder.itemView, "목록을 삭제되었어요", Snackbar.LENGTH_LONG)
                    .setAction("되돌리기", View.OnClickListener {
                        adapter.restoreItem(item, index)
                        presenter?.savePreference(preference, histories)
                    })
                    .setActionTextColor(Color.YELLOW)
                    .show()
        }
    }

}
