package ruzanna.game.contacts

import android.os.Parcel
import android.os.Parcelable

data class Contact(val name:String?,
                   val phoneNumbes:MutableList<PhoneNumber>,
                   val company:String?,
                   val photoUrl:String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.createTypedArrayList(PhoneNumber)!!,
        parcel.readString(),
        parcel.readString()
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeTypedList(phoneNumbes)
        parcel.writeString(company)
        parcel.writeString(photoUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Contact> {
        override fun createFromParcel(parcel: Parcel): Contact {
            return Contact(parcel)
        }

        override fun newArray(size: Int): Array<Contact?> {
            return arrayOfNulls(size)
        }
    }
}

data class PhoneNumber(val phoneNumber:String?,
                   val phoneNumberType:String?

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(phoneNumber)
        parcel.writeString(phoneNumberType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhoneNumber> {
        override fun createFromParcel(parcel: Parcel): PhoneNumber {
            return PhoneNumber(parcel)
        }

        override fun newArray(size: Int): Array<PhoneNumber?> {
            return arrayOfNulls(size)
        }
    }
}