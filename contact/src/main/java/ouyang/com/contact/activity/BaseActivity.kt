package ouyang.com.contact.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.support.v4.app.ActivityCompat
import android.content.Intent
import android.os.Build
import android.net.Uri
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.widget.Toast
import android.widget.Toast.makeText
import ouyang.com.contact.R


open class BaseActivity : AppCompatActivity() {

    private lateinit var builder: AlertDialog.Builder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun shortToast(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }


    /**
     * 判断权限集合
     *
     * @param permissions
     * @return true 缺少权限    false-权限已给予
     */
    private fun lacksPermissions(vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (lacksPermission(permission)) {
                return true
            }
        }
        return false
    }

    /**
     * 判断是否缺少权限
     *
     * @param permission
     * @return true:缺少;false:已申请权限
     */
    private fun lacksPermission(permission: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED
        } else false
    }

    /**
     * 请求权限兼容低版本
     */
   private fun requestPermissions(code: Int, vararg permissions: String) {
        ActivityCompat.requestPermissions(this, permissions, code)
    }

    /**
     * 显示缺失权限提示
     */
    private fun showMissingPermissionDialog(code: Int, vararg permissions: String) {
        builder = AlertDialog.Builder(this)
        //        builder.setCancelable(false);
        builder.setTitle(R.string.help)
        builder.setMessage(R.string.string_help_text)

        builder.setNegativeButton(R.string.quit, null)

        builder.setPositiveButton(R.string.settings, { _, _ ->
            //如果不是特定的pos机默认当成手机处理,打开应用-在设置中赋予权限
            startAppSettings()
        })

        builder.show()
    }

    /**
     * 启动应用的设置
     */
    private fun startAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = (Uri.parse("package:" + packageName))
        startActivity(intent)
    }

    /**
     * 是否需要程序弹框说明该权限的说明
     *
     * @param permissions
     * @return
     */
    private fun shouldShowRationale(vararg permissions: String): Boolean {
        val count = permissions.count { ActivityCompat.shouldShowRequestPermissionRationale(this, it) && ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_DENIED }
//        for (permission in permissions) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission) && ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
//                count++
//            }
//        }
        return count == permissions.size
    }

    /**
     * 检查权限
     *
     * @param permissions
     * @param code        请求码, 用来区分同时请求多个权限时的每个权限的结果
     */
    fun checkPermissions(code: Int, vararg permissions: String): Boolean {

        return if (lacksPermissions(*permissions)) {
            if (shouldShowRationale(*permissions)) {
                showMissingPermissionDialog(code, *permissions)
            } else {
                requestPermissions(code, *permissions)
            }
            false
        } else {
            true
        }
    }
}
