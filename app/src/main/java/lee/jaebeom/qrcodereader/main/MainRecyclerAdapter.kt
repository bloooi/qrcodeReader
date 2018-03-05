package lee.jaebeom.qrcodereader.main

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_main.view.*
import lee.jaebeom.qrcodereader.History
import lee.jaebeom.qrcodereader.R
import lee.jaebeom.qrcodereader.WebActivity
import lee.jaebeom.qrcodereader.util.Checker
import java.util.*

/**
 * Created by leejaebeom on 2018. 2. 16..
 */
class MainRecyclerAdapter(private val context: Context, private val histories: ArrayList<History>) : RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MainViewHolder =
            MainViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_main, parent, false))

    override fun getItemCount(): Int = histories.size

    override fun onBindViewHolder(holder: MainViewHolder?, position: Int) {
        holder?.bind(histories[position])
        holder?.itemView?.setOnClickListener {
            if(Checker.checkData(histories[position].content) == "URL"){
                val intent = Intent(context, WebActivity::class.java)
                intent.putExtra("url", histories[position].content)
                intent.putExtra("name", histories[position].name)
                context.startActivity(intent)
            }else{
                Snackbar.make(holder.itemView, "텍스트 뷰어는 준비중이에요. ㅠㅠ", Snackbar.LENGTH_LONG).show()
            }
        }

        holder?.itemView?.setOnLongClickListener {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("url", histories[position].content)
            clipboard.primaryClip = clip
            Snackbar.make(holder.itemView, "내용이 복사되었어요!", Snackbar.LENGTH_LONG).show()
            return@setOnLongClickListener true
        }

    }

    class MainViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){
        lateinit var history: History
        fun bind(history: History){
            this.history = history
            itemView?.text?.text = history.name
            itemView?.url?.text = history.content
            itemView?.time?.text = history.time
        }
    }
}
