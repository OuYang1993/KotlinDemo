package ouyang.com.contact.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.item_contact.view.*
import ouyang.com.contact.R
import ouyang.com.contact.bean.Contact
import ouyang.com.contact.databinding.ItemContactBinding

/**
 * Created by admin on 2017/10/10.
 * 联系人适配器
 */

class ContactAdapter(mContext: Context, mContacts: List<Contact>) : RecyclerView.Adapter<ContactAdapter.MyViewHolder>() {

    private var context = mContext
    private var contacts = mContacts
    private var inflater = LayoutInflater.from(context)
    lateinit var binding: ItemContactBinding


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        val view = inflater.inflate(R.layout.item_contact, parent, false)
        binding = DataBindingUtil.bind(view)
        return MyViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    override fun getItemCount(): Int {
        return contacts.size
    }


    inner class MyViewHolder : RecyclerView.ViewHolder {
        //添加inner标记注明该类为内部类,不然无法访问到外部类的变量
        var tvName: TextView
        var tvNumber: TextView

        constructor(view: View) : super(view) {
            tvName = view.tv_name
            tvNumber = view.tv_number
        }

        fun bind(contact: Contact) {
            binding.contact = contact
        }
    }
}