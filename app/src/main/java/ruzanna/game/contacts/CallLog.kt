package ruzanna.game.contacts

import android.os.Parcel
import android.os.Parcelable

data class CallLog(
    val phoneNumber: String?,
    val callType: String?,
    val callDate: String?,
    val callDuration:Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(phoneNumber)
        parcel.writeString(callType)
        parcel.writeString(callDate)
        parcel.writeInt(callDuration)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CallLog> {
        override fun createFromParcel(parcel: Parcel): CallLog {
            return CallLog(parcel)
        }

        override fun newArray(size: Int): Array<CallLog?> {
            return arrayOfNulls(size)
        }
    }
}