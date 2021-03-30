package ruzanna.game.contacts

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    var contactList: MutableList<Contact> = mutableListOf()
    lateinit var listener : ContactListAdapterItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.contact_row, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.contact = contactList[position]
        holder.itemView.setOnClickListener {
            listener.onItemClicked(contactList[position])
        }
        holder.itemView.setOnLongClickListener {
            listener.onItemLongClicked(contactList[position])
            true
        }
        holder.init()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        lateinit var contact: Contact

        fun init() {
            itemView.apply {
                findViewById<TextView>(R.id.name).text = contact.name
                if (contact.phoneNumbes.isNotEmpty()) {
                    findViewById<TextView>(R.id.phone).text = contact.phoneNumbes[0].phoneNumber
                }
                val icon = findViewById<ImageView>(R.id.imageView)
                if (contact.photoUrl != null) {
                    icon.setImageURI(Uri.parse(contact.photoUrl))
                }else{
                    icon.setImageResource(R.drawable.baseline_account_circle_black_48dp)
                }
            }
            itemView.invalidate()
        }
    }

    interface ContactListAdapterItemClickListener {
        fun onItemClicked(contact: Contact)
        fun onItemLongClicked(contact: Contact)
    }

}