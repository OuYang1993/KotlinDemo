package ouyang.com.contact.util

import android.text.TextUtils
import ouyang.com.contact.bean.Contact

/**
 * Created by admin on 2017/10/10.
 */
class BindingUtil {
    companion object {
        fun getPhoneNumber(contact: Contact): String {
            val sb = StringBuilder()
            val numbers = contact.number
            numbers?.forEach { value ->
                if (!TextUtils.isEmpty(value)) {
                    sb.append(value)
                    sb.append("\t")
                }
            }
            return sb.toString()
        }
    }

}