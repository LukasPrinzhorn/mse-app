package com.example.weihnachtmaerkte.entities

import android.os.Parcel
import android.os.Parcelable

data class Rating(val id: Long = 0L,
                  var ambience: Float = 0f,
                  var food: Float = 0f,
                  var drinks: Float = 0f,
                  var crowding: Float = 0f,
                  var family: Float = 0f,
                  var title: String? = null,
                  var text: String? = null,
                  var userid: String = ""
) : Parcelable{
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString().toString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeFloat(ambience)
        parcel.writeFloat(food)
        parcel.writeFloat(drinks)
        parcel.writeFloat(crowding)
        parcel.writeFloat(family)
        parcel.writeString(title)
        parcel.writeString(text)
        parcel.writeString(userid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Rating> {
        override fun createFromParcel(parcel: Parcel): Rating {
            return Rating(parcel)
        }

        override fun newArray(size: Int): Array<Rating?> {
            return arrayOfNulls(size)
        }
    }
}