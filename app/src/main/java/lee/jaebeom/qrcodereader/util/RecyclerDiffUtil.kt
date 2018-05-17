package lee.jaebeom.qrcodereader.util

import android.support.v7.util.DiffUtil
import lee.jaebeom.qrcodereader.History

class RecyclerDiffUtil(private val oldList: List<History>, private val newList: List<History>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
            = oldList[oldItemPosition].time == newList[newItemPosition].time

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
            = oldList[oldItemPosition] == newList[newItemPosition]
}