package ouyang.com.demo.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Created by OuYang on 2018/1/11.
 */

class MyGridLayout : android.support.v7.widget.GridLayout {
    private val TAG = javaClass.simpleName
    private var columnCount = "1"
    private var rowCount = "1"

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        for (i in 0 until attrs.attributeCount) {
            val attrName = attrs.getAttributeName(i)
            val attrValue = attrs.getAttributeValue(i)
            if ("columnCount" == attrName) {
                columnCount = attrValue
                Log.i(TAG, "columnCount $columnCount")
            }
            if ("rowCount" == attrName) {
                rowCount = attrValue
                Log.i(TAG, "columnCount $columnCount")
            }
            Log.e(TAG, "attrName $attrName\tattrValue $attrValue")

        }
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val measuredHeight = measuredHeight
        val measuredWidth = measuredWidth
        Log.d(TAG, "onMeasure: measuredHeight $measuredHeight\tmeasuredWidth:  $measuredWidth")

        setMeasuredDimension(measuredWidth, measuredHeight)
        Log.i(TAG, "onMeasure: widthMode: AT_MOST: " + (widthMode == View.MeasureSpec.AT_MOST) + "\tEXACTLY: " + (widthMode == View.MeasureSpec.EXACTLY) + "\tUNSPECIFIED: " + (widthMode == View.MeasureSpec.UNSPECIFIED))
        Log.i(TAG, "onMeasure: heightMode: AT_MOST: " + (heightMode == View.MeasureSpec.AT_MOST) + "\tEXACTLY: " + (heightMode == View.MeasureSpec.EXACTLY) + "\tUNSPECIFIED: " + (heightMode == View.MeasureSpec.UNSPECIFIED))


        val childCount = childCount
        var maxChildSize = 0
        if (measuredHeight > measuredWidth) {
            //高比宽大
            maxChildSize = measuredWidth / Integer.parseInt(columnCount)
        } else {
            maxChildSize = measuredHeight / Integer.parseInt(rowCount)
        }
        Log.e(TAG, "maxChildWidth  $maxChildSize")
        for (i in 0 until childCount) {
            val widthSpec = MeasureSpec.makeMeasureSpec(maxChildSize, MeasureSpec.EXACTLY)
            getChildAt(i)?.measure(widthSpec, widthSpec)
            val childMeasureWidth = getChildAt(i)?.measuredWidth
            val childMeasureHeight = getChildAt(i)?.measuredHeight
            Log.e(TAG, "onMeasure:$i  childMeasuredHeight $childMeasureHeight\tchildMeasuredWidth:  $childMeasureWidth")

        }


    }


}
