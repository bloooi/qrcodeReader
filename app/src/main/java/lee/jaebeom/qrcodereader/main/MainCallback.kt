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


/**
 * Created by leejaebeom on 2018. 3. 23..
 */

enum class ButtonState{
    GONE,
    LEFT_VISIBLE,
    RIGHT_VISIBLE
}
class MainCallback : ItemTouchHelper.Callback() {
    private var swipeBack: Boolean = false
    private var buttonShowState: ButtonState = ButtonState.GONE
    private var buttonWidth = 300
    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int = makeMovementFlags(0, LEFT or RIGHT)

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack){
            swipeBack = false
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onChildDraw(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        if (actionState == ACTION_STATE_SWIPE){
            setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        drawButtons(c, viewHolder)
    }

    private fun drawButtons(c: Canvas?, viewHolder: RecyclerView.ViewHolder?){
        val buttonWidthWithoutPadding: Float = (buttonWidth - 20).toFloat()
        val corners: Float = 16F

        val itemView: View? = viewHolder?.itemView
        val p = Paint()

        val rightButton = RectF(itemView!!.right - buttonWidthWithoutPadding, itemView!!.top.toFloat(), itemView!!.right.toFloat(), itemView!!.bottom.toFloat())
        p.color = Color.RED
        c?.drawRect(rightButton, p)
        drawText("삭제", c, rightButton, p)

        var buttonInstance: RectF? = null

        if (buttonShowState == ButtonState.RIGHT_VISIBLE){
            buttonInstance = rightButton
        }
    }

    fun drawText(text: String, c: Canvas?, button: RectF, p: Paint){
        val textSize: Float = 60F
        p.apply {
            color = Color.WHITE
            isAntiAlias = true
            this.textSize = textSize
            val textWidth: Float = p.measureText(text)
            c?.drawText(text, button.centerX() - (textWidth/2), button.centerY() + (textSize/2), this)
        }


    }

    private fun setTouchListener(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean){
        recyclerView?.setOnTouchListener { _, event ->
            swipeBack = event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP
            if (swipeBack){
                if (dX < -buttonWidth)
                    buttonShowState = ButtonState.RIGHT_VISIBLE
                else if (dX > buttonWidth)
                    buttonShowState = ButtonState.LEFT_VISIBLE

                if (buttonShowState != ButtonState.GONE){
                    setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    setItemsClickable(recyclerView, false)
                }
            }
            false
        }
    }

    private fun setTouchDownListener(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean){
        recyclerView?.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
            false
        }
    }


    private fun setTouchUpListener(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean){
        recyclerView?.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP){
                super@MainCallback.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                recyclerView.setOnTouchListener { _, _ ->
                    false
                }
                setItemsClickable(recyclerView, true)
                swipeBack = false
                buttonShowState = ButtonState.GONE
            }
            false
        }
    }

    private fun setItemsClickable(recyclerView: RecyclerView?, isClickable: Boolean){
        for (i in 0 until recyclerView?.childCount!!) {
            recyclerView.getChildAt(i).isClickable = isClickable
        }
    }
}