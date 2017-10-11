package ouyang.com.contact.bean

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.text.TextUtils
import ouyang.com.contact.BR

/**
 * Created by admin on 2017/10/10.
 * 联系人
 */

data class Contact(var _id: Int?, var _name: String?, var _number: ArrayList<String>?) : BaseObservable() {

    var id: Int?
        @Bindable get() = _id
        set(value) {
            _id = value
            notifyPropertyChanged(BR.id)
        }
    var name: String?
        @Bindable get() = _name
        set(value) {
            _name = value
            notifyPropertyChanged(BR.name)
        }
    var number: ArrayList<String>?
        @Bindable get() = _number
        set(value) {
            _number = value
            notifyPropertyChanged(BR.number)
        }

    var numberString: String?
        @Bindable get() = getMyNumber()
        set(value) {
            numberString = getMyNumber()
            notifyPropertyChanged(BR.numberString)
        }

    fun getMyNumber(): String? {
        val sb = StringBuilder()
        number?.forEach { value ->
            if (!TextUtils.isEmpty(value)) {
                sb.append(value)
                sb.append("\n")
            }
        }
        return sb.substring(0, sb.toString().lastIndexOf("\n"))
    }


    override fun toString(): String {
        return "Contact(id=$_id, name='$_name', number='$_number')"
    }


}