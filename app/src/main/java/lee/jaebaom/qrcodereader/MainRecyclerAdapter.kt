package lee.jaebaom.qrcodereader

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_main.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
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
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra("url", histories[position].content)
            intent.putExtra("name", histories[position].name)
            context.startActivity(intent)
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
