package om.demo.display

import android.databinding.DataBindingUtil
import android.graphics.Point
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import om.demo.display.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        hideNav()
        getDisplayInfo()
    }

    private fun hideNav() {
        window.decorView.systemUiVisibility = (
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or//让应用的主体内容全屏,即会隐藏actionbar
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

    }

    private fun getDisplayInfo() {
        val point = Point()
        windowManager.defaultDisplay.getRealSize(point)
        val densityDpi = resources.displayMetrics.densityDpi
        val width = point.x
        val height = point.y
        val density = resources.displayMetrics.density
        val scaledDensity = resources.displayMetrics.scaledDensity

        val builder = StringBuilder()
        builder.append("densityDpi: ").append(densityDpi).append("\n")
                .append("width: ").append(width).append("\n")
                .append("height: ").append(height).append("\n")
                .append("density: ").append(density).append("\n")
                .append("scaledDensity: ").append(scaledDensity).append("\n")

        binding.tvMsg.text = builder.toString()
    }
}
