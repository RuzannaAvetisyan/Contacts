package ruzanna.game.contacts

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_contact_delete.*

class ContactDeleteFragment: DialogFragment() {
    lateinit var listener:ContactDeleteListener
    lateinit var contact: Contact

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_delete, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contact_name.text = "You want to remove ${contact.name} from your contacts"
        yes.setOnClickListener {
            listener.onDeleteListener(true, contact)
        }
        no.setOnClickListener {
            listener.onDeleteListener(false, contact)
        }
    }

    interface ContactDeleteListener{
        fun onDeleteListener(b: Boolean, contact: Contact)
    }

}