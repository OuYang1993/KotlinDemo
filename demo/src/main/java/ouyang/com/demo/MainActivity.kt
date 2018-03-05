package ouyang.com.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.GridLayout
import android.view.Gravity
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        for (i in 0 until 12) {//行数
            val button = TextView(this)
            val rowSpec = GridLayout.spec(i / 3,  1f)
            val columnSpec = GridLayout.spec(i % 3,  1f)
            val margin = 8
            val params = GridLayout.LayoutParams(rowSpec, columnSpec)
            params.setMargins(margin, margin, margin, margin)


            button.width = 0
            button.height = 0
            button.minHeight = 0
            button.minWidth = 0
            button.text = "1"
            button.gravity = Gravity.CENTER
            button.setPadding(0, 0, 0, 0)
            button.setTextColor(resources.getColor(R.color.white))
            button.background = resources.getDrawable(R.color.colorPrimary)
            grid.addView(button, params)

        }
    }
}
