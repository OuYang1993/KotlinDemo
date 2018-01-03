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
        fun getContacts(context: Context): MutableList<Contact> {
            val list = ArrayList<Contact>(0)
            val cr = context.contentResolver
            val cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY)
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
                        var phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
//                        Log.e("ContactUtil", "phoneNumber: " + phoneNumber)
                        if (!TextUtils.isEmpty(phoneNumber)) {
                            phoneNumber = phoneNumber.replace(" ", "", true)
                            val length = phoneNumber.length
                            if (length > 11 && phoneNumber.startsWith("+")) {//长度大于11的 表示前面带有+86之类的,为了避免电话号码也被裁剪,以+开头的才算
                                phoneNumber = phoneNumber.substring(length - 11)//手机号码固定为11位,所以需要截取后面的11位
                            }
                            numbers.add(phoneNumber)
                        }
                    }
                    phoneCursor.close()
                    contact.number = numbers
//                    Log.e("ContactUtil", contact.toString())
                    list.add(contact)

                }
                cursor.close()
            }
            return list
        }
    }

}