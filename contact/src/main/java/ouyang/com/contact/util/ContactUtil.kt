package ouyang.com.contact.util

import android.content.Context
import android.provider.ContactsContract
import android.text.TextUtils
import ouyang.com.contact.bean.Contact

/**
 * Created by admin on 2017/10/10.
 * 联系人工具类
 */

class ContactUtil {
    companion object { //companion相当于java的static,只有在这里面的方法才能以静态方法的方式调用
        fun getContacts(context: Context): List<Contact> {
            val list = ArrayList<Contact>(0)
            val cr = context.contentResolver
            val cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val _id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val _name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val contact = Contact(_id, _name, null)
                    val phoneCursor = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + _id, null, null)
                    var numbers = ArrayList<String>()
                    while (phoneCursor.moveToNext()) {
                        val phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
//                        Log.e("ContactUtil", "phoneNumber: " + phoneNumber)
                        if (!TextUtils.isEmpty(phoneNumber)) {
                            numbers.add(phoneNumber)
                        }
                    }
                    phoneCursor.close()
                    contact.number = numbers
//                    Log.e("ContactUtil", contact.toString())
                    list.add(contact)

                }
            }
            cursor.close()
            return list
        }
    }

}