package ruzanna.game.contacts

import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.CallLog
import android.provider.ContactsContract
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.Long
import java.util.*


class MainActivity : AppCompatActivity(), ContactListFragment.ContactListFragmentListener,
    ContactInfoFragment.ContactInfoFragmentBackListener,
    ContactDeleteFragment.ContactDeleteListener {
    private var contactList: MutableList<Contact> = mutableListOf()
    private var callDetailsMap: MutableMap<String, ruzanna.game.contacts.CallLog> = mutableMapOf()
    private val contactListFragment = ContactListFragment()
    private val contactInfoFragment = ContactInfoFragment()
    private lateinit var contactDeleteFragment: ContactDeleteFragment

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (contactList.isEmpty()) {
            contactList = getContactList()
        }
        contactListFragment.contactList = contactList
        contactListFragment.listener = this
        supportFragmentManager.beginTransaction().add(R.id.container, contactListFragment).commit()
        if(callDetailsMap.isEmpty()){
            callDetailsMap = getCallDetails()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getContactList(): MutableList<Contact> {
        val contacts = mutableListOf<Contact>()
        val cr = contentResolver
        val cur: Cursor? = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null)
        if ((cur?.count ?: 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                val id: String = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID))
                val name: String = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME))
                val icon: String? = cur.getString(cur.getColumnIndex(
                    ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))
                val rawContactId: String? = getRawContactId(id)
                val companyName: String? = rawContactId?.let { getCompanyName(it) }
                val contItem = Contact(name, mutableListOf(), companyName, icon)
                if (cur.getInt(cur.getColumnIndex(
                                ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val pCur: Cursor? = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", arrayOf(id), null)
                    while (pCur!!.moveToNext()) {
                        val phoneNo: String = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER))
                        val phoneNoTyp: String = pCur.getString(pCur.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.TYPE))
                        val d = when(phoneNoTyp){
                            "3" ->"HOME"
                            "2"->"MOBILE"
                            else -> "OTHER"
                        }
                        contItem.phoneNumbes.add(PhoneNumber(phoneNo, d))
                    }
                    pCur.close()
                }
                contacts.add(contItem)
            }
        }
        cur?.close()
        return contacts
    }
    @SuppressLint("Recycle")
    private fun getCompanyName(rawContactId: String): String? {
        return try {
            val orgWhere =
                ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?"
            val orgWhereParams = arrayOf(
                rawContactId,
                ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE
            )
            val cursor: Cursor = contentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                null, orgWhere, orgWhereParams, null
            )
                ?: return null
            var name: String? = null
            if (cursor.moveToFirst()) {
                name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY))
            }
            cursor.close()
            name
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    @SuppressLint("Recycle")
    private fun getRawContactId(contactId: String): String? {
        val projection =
            arrayOf(ContactsContract.RawContacts._ID)
        val selection = ContactsContract.RawContacts.CONTACT_ID + "=?"
        val selectionArgs = arrayOf(contactId)
        val c: Cursor = contentResolver.query(
            ContactsContract.RawContacts.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )
            ?: return null
        var rawContactId = -1
        if (c.moveToFirst()) {
            rawContactId = c.getInt(c.getColumnIndex(ContactsContract.RawContacts._ID))
        }
        c.close()
        return rawContactId.toString()
    }

    private fun getCallDetails(): MutableMap<String, ruzanna.game.contacts.CallLog> {
        val callDetailsMap = mutableMapOf<String, ruzanna.game.contacts.CallLog>()
        val managedCursor = managedQuery(
            CallLog.Calls.CONTENT_URI, null,
            null, null, null
        )
        val number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER)
        val type = managedCursor.getColumnIndex(CallLog.Calls.TYPE)
        val date = managedCursor.getColumnIndex(CallLog.Calls.DATE)
        val duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION)
        while (managedCursor.moveToNext()) {
            val phNumber = managedCursor.getString(number)
            val callType = managedCursor.getString(type)
            val callDate = managedCursor.getString(date)
            val callDayTime = Date(Long.valueOf(callDate)).toString()
            val callDuration = managedCursor.getString(duration)
            var dir: String? = null
            when (callType.toInt()) {
                CallLog.Calls.OUTGOING_TYPE -> dir = "OUTGOING"
                CallLog.Calls.INCOMING_TYPE -> dir = "INCOMING"
                CallLog.Calls.MISSED_TYPE -> dir = "MISSED"
            }
            callDetailsMap[phNumber] = CallLog(phNumber, dir, callDayTime, callDuration.toInt())

        }
        managedCursor.close()
        return callDetailsMap
    }

    override fun onContactItemClicked(contact: Contact) {
        contactInfoFragment.listener = this
        contactInfoFragment.contact = contact
        if (contact.phoneNumbes.isNotEmpty()){
            contactInfoFragment.callLog = callDetailsMap[contact.phoneNumbes[0].phoneNumber]
        }
        supportFragmentManager.beginTransaction().replace(R.id.container, contactInfoFragment).commit()
    }

    override fun onContactItemLongClicked(contact: Contact) {
        contactDeleteFragment = ContactDeleteFragment()
        contactDeleteFragment.listener = this
        contactDeleteFragment.contact = contact
        contactDeleteFragment.show(supportFragmentManager, "ContactDeleteFragment")
    }

    override fun onBackListener() {
        contactListFragment.contactList = contactList
        contactListFragment.listener = this
        supportFragmentManager.beginTransaction().replace(R.id.container, contactListFragment).commit()
    }

    override fun onDeleteListener(b: Boolean, contact: Contact) {
        contactDeleteFragment.dismiss()
        if (b){
            contactList.remove(contact)
        }
        contactListFragment.notifyDataChanged()
    }

}
