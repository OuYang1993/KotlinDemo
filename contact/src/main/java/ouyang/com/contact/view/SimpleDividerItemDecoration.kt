package ouyang.com.contact.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import ouyang.com.contact.R


/**
 * Created by OuYang on 2016/1/27.
 * recyclerView 分割线
 * 使用: recyclerView.addItemDecoration(new SimpleDividerItemDecoration(...))
 */
class SimpleDividerItemDecoration : RecyclerView.ItemDecoration {
    private var mDivider: Drawable
    private var paddingLeft: Int
    private var paddingRight: Int

    constructor(context: Context, dividerResId: Int) {
        mDivider = ContextCompat.getDrawable(context, dividerResId)
        paddingLeft = context.resources.getDimensionPixelSize(R.dimen.rv_divider_paddingLeft)
        paddingRight = context.resources.getDimensionPixelSize(R.dimen.rv_divider_paddingRight)
    }

    constructor(context: Context) {
        mDivider = ContextCompat.getDrawable(context, R.drawable.line_divider)
        paddingLeft = context.resources.getDimensionPixelSize(R.dimen.rv_divider_paddingLeft)
        paddingRight = context.resources.getDimensionPixelSize(R.dimen.rv_divider_paddingRight)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        val left = parent.paddingLeft.plus(paddingLeft)
        val right = parent.width.minus(parent.paddingRight).minus(paddingRight)

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + mDivider.intrinsicHeight
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }
}
