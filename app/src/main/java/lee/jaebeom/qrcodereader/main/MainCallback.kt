package lee.jaebeom.qrcodereader.main


import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.helper.ItemTouchHelper.*
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Button
import kotlinx.android.synthetic.main.item_url.view.*
import lee.jaebeom.qrcodereader.History


/**
 * Created by leejaebeom on 2018. 3. 23..
 */

class MainCallback(dragDirs: Int, swipeDirs: Int, private val listener: OnItemHelperListener) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean = true

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        val foregroundView = (viewHolder as? MainRecyclerAdapter.MainViewHolder)?.itemView?.content
        getDefaultUIUtil().onSelected(foregroundView)
    }

    override fun onChildDrawOver(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val foregroundView = (viewHolder as? MainRecyclerAdapter.MainViewHolder)?.itemView?.content
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
    }

    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?) {
        val foregroundView = (viewHolder as? MainRecyclerAdapter.MainViewHolder)?.itemView?.content
        Callback.getDefaultUIUtil().clearView(foregroundView)
    }

    override fun onChildDraw(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder
    ?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val foregroundView = (viewHolder as? MainRecyclerAdapter.MainViewHolder)?.itemView?.content
        Callback.getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
        viewHolder?.run {
            listener.onSwiped(this, direction, this.adapterPosition)
        }
    }

//    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
//        return super.convertToAbsoluteDirection(flags, layoutDirection)
//    }

    interface OnItemHelperListener{
        fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int)
    }
}