package ouyang.com.contact.activity

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import ouyang.com.contact.R
import ouyang.com.contact.adapter.ContactAdapter
import ouyang.com.contact.util.ContactUtil
import ouyang.com.contact.view.SimpleDividerItemDecoration

class MainActivity : BaseActivity() {
    private val TAG: String = this.javaClass.simpleName
    private val REQUEST_CONTACT: Int = 100
    private lateinit var adapter: ContactAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val hasPermission = checkPermissions(REQUEST_CONTACT, android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.WRITE_CONTACTS)
        if (hasPermission) {
            displayContacts()
        }
    }

    private fun displayContacts() {
        val list = ContactUtil.getContacts(this)
        Log.e(TAG, "contact count: " + list.size)
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
