package com.example.weihnachtmaerkte.entities

import android.os.Parcel
import android.os.Parcelable


data class Market(val id: Long,
                  var name: String,
                  var address: String,
                  var coordinates: DoubleArray?,
                  var dates: String,
                  var openingHours: String,
                  var weblink: String,
                  var ratings: ArrayList<Long>?,
                  var image: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.createDoubleArray(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readArrayList(Market::class.java.classLoader)?.filterIsInstance<Long>() as ArrayList<Long>,
            parcel.readString().toString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(address)
        parcel.writeDoubleArray(coordinates)
        parcel.writeString(dates)
        parcel.writeString(openingHours)
        parcel.writeString(weblink)
        parcel.writeList(ratings as List<Long>?)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Market> {
        override fun createFromParcel(parcel: Parcel): Market {
            return Market(parcel)
        }

        override fun newArray(size: Int): Array<Market?> {
            return arrayOfNulls(size)
        }
    }

}