package ruzanna.game.contacts

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_contact_info.*

class ContactInfoFragment:Fragment() {
    lateinit var listener : ContactInfoFragmentBackListener
    lateinit var contact: Contact
    var callLog: CallLog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        name.text = contact.name
        if(contact.company != null){
            company_name.text = contact.company
        }
        if(contact.phoneNumbes.isNotEmpty()){
            phone.text = contact.phoneNumbes[0].phoneNumber
            phone_type.text = contact.phoneNumbes[0].phoneNumberType
        }
        if(contact.photoUrl != null){
            icon.setImageURI(Uri.parse(contact.photoUrl))
        }
        if(callLog != null){
            call_type.text = callLog!!.callType
            call_date.text = callLog!!.callDate
            call_duration.text = callLog!!.callDuration.toString()
        }
        back.setOnClickListener {
            listener.onBackListener()
        }
    }

    interface ContactInfoFragmentBackListener {
        fun onBackListener()
    }
}