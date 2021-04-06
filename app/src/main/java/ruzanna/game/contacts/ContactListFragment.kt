package ruzanna.game.contacts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ContactListFragment:Fragment() {
    lateinit var contactList: MutableList<Contact>
    lateinit var listener : ContactListFragmentListener
    private val adapter = ContactAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contentRecyclerView = view.findViewById<RecyclerView>(R.id.content_recyclerView)
        adapter.contactList = contactList
        contentRecyclerView.adapter = adapter
        contentRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter.notifyDataSetChanged()
        adapter.listener = object : ContactAdapter.ContactListAdapterItemClickListener {
            override fun onItemClicked(contact: Contact) {
                listener.onContactItemClicked(contact)
            }
            override fun onItemLongClicked(contact: Contact) {
                listener.onContactItemLongClicked(contact)
            }

        }
    }

    fun notifyDataChanged() {
        adapter.notifyDataSetChanged()
    }


    interface ContactListFragmentListener {
        fun onContactItemClicked(contact: Contact)
        fun onContactItemLongClicked(contact: Contact)
    }


}