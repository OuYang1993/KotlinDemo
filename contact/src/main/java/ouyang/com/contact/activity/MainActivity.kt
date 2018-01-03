package ouyang.com.contact.activity

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import ouyang.com.contact.R
import ouyang.com.contact.adapter.ContactAdapter
import ouyang.com.contact.bean.Contact
import ouyang.com.contact.util.ContactUtil
import ouyang.com.contact.view.SimpleDividerItemDecoration
import kotlin.collections.ArrayList

const val REQUEST_CONTACT: Int = 100//const 对应java的静态变量,public属性, const只能用在顶级属性(所谓的顶级属性 就是不依赖于类,需要写在类外面的)

class MainActivity : BaseActivity() {
    private val TAG: String = this.javaClass.simpleName
    private lateinit var adapter: ContactAdapter
    private lateinit var shortcutManager: ShortcutManager
    private lateinit var list: MutableList<Contact>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val hasPermission = checkPermissions(REQUEST_CONTACT, android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.WRITE_CONTACTS, android.Manifest.permission.CALL_PHONE)
        if (hasPermission) {
            displayContacts()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1)
            addDynamicShortcuts()

    }

    @TargetApi(Build.VERSION_CODES.N_MR1)
    private fun addDynamicShortcuts() {

        shortcutManager = getSystemService(Context.SHORTCUT_SERVICE) as ShortcutManager
        val shortcuts = ArrayList<ShortcutInfo>()
        if (list.isNotEmpty()) {
            shortcutManager.removeAllDynamicShortcuts()
            for (i in 0 until 4) {
                val num = list[i].number!![0]
                val content = list[i].name + "\t" + num
                if (TextUtils.isEmpty(content))
                    continue
                val shortcutInfo: ShortcutInfo = ShortcutInfo.Builder(this, "contact" + i)
                        .setShortLabel(content)
                        .setLongLabel(content)
                        .setIntents(arrayOf(Intent(Intent.ACTION_VIEW, Uri.EMPTY, this, MainActivity::class.java),
                                Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)))
                        .setIcon(Icon.createWithResource(this, R.mipmap.ic_launcher_round))
                        .build()
                shortcuts.add(shortcutInfo)
            }
            shortcutManager.addDynamicShortcuts(shortcuts)
//            shortcutManager.dynamicShortcuts = shortcuts
        }
        val numbers: MutableList<String?> = ArrayList(0)
        for (i in 0 until 11) {
            numbers.add(i, if (i % 2 == 0) "整数" else null)
        }

        val nullCount = numbers.filter { it != null }.count()
        val nonNullCount = numbers.count { it == null }
        Log.e(TAG, "Null count:  $nullCount \tNonNull count:  $nonNullCount")
    }


    private fun displayContacts() {
        list = ContactUtil.getContacts(this)
        Log.e(TAG, "contact count: " + list.size)
//        Collections.sort(list, { o1, o2 ->    //利用Collection排序,将中文转换成拼音,再根据首字母排序,但是遇到多音字可能会有错乱
//            val formatter = HanyuPinyinOutputFormat()
//            formatter.toneType = HanyuPinyinToneType.WITHOUT_TONE
//            formatter.caseType = HanyuPinyinCaseType.UPPERCASE
//            val of1 = PinyinHelper.toHanyuPinyinStringArray(o1.name!!.elementAt(0), formatter)
//            val of2 = PinyinHelper.toHanyuPinyinStringArray(o2.name!!.elementAt(0), formatter)
//            of1[0].toString().compareTo(of2[0].toString())
//        })
        rv_contact.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = ContactAdapter(this, list)
        rv_contact.adapter = adapter
        val divider = SimpleDividerItemDecoration(applicationContext)
        rv_contact.addItemDecoration(divider)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CONTACT) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayContacts()
            } else {
                shortToast("请赋予相应权限")
            }
        }
    }
}
