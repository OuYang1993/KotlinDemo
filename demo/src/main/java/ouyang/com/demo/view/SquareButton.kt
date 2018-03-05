package ouyang.com.demo.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Created by OuYang on 2018/1/11.
 */

class SquareButton : android.support.v7.widget.AppCompatButton {
    private val TAG = javaClass.simpleName

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        val measuredHeight = measuredHeight
        val measuredWidth = measuredWidth
        Log.d(TAG, "onMeasure: measuredHeight $measuredHeight\tmeasuredWidth:  $measuredWidth")

        Log.i(TAG, "onMeasure: widthMode: AT_MOST: " + (widthMode == View.MeasureSpec.AT_MOST) + "\tEXACTLY: " + (widthMode == View.MeasureSpec.EXACTLY) + "\tUNSPECIFIED: " + (widthMode == View.MeasureSpec.UNSPECIFIED))
        Log.i(TAG, "onMeasure: heightMode: AT_MOST: " + (heightMode == View.MeasureSpec.AT_MOST) + "\tEXACTLY: " + (heightMode == View.MeasureSpec.EXACTLY) + "\tUNSPECIFIED: " + (heightMode == View.MeasureSpec.UNSPECIFIED))
        if ((widthMode == MeasureSpec.EXACTLY) or (widthMode == MeasureSpec.AT_MOST) or (widthMode == MeasureSpec.UNSPECIFIED)) {
            var size = 0
            if (measuredWidth > measuredHeight)
                size = measuredHeight//如果宽度比高度大,那么用小的一边
            if (measuredHeight > measuredWidth)
                size = measuredWidth

            Log.e(TAG, "setMeasuredDimension $size ")
            setMeasuredDimension(size, size)
        }
        val remeasuredHeight = getMeasuredHeight()
        val remeasuredWidth = getMeasuredWidth()
        Log.e(TAG, "onMeasure: reMeasuredHeight $remeasuredHeight\treMeasuredWidth:  $remeasuredWidth")
    }


}
