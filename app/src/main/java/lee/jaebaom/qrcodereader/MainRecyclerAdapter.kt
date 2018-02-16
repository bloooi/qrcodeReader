package lee.jaebaom.qrcodereader

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_main.view.*

/**
 * Created by leejaebeom on 2018. 2. 16..
 */
class MainRecyclerAdapter(private val histories: ArrayList<History>) : RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MainViewHolder =
            MainViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_main, parent, false))

    override fun getItemCount(): Int = histories.size

    override fun onBindViewHolder(holder: MainViewHolder?, position: Int) {
        holder?.bind(histories[position].name)
    }

    class MainViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(text: String){
            itemView?.text?.text = text
        }
    }
}
