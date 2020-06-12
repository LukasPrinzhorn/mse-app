package com.example.weihnachtmaerkte.entities

import android.os.Parcel
import android.os.Parcelable


data class Market(val id: String,
                  var name: String,
                  var address: String,
                  var coordinates: DoubleArray?,
                  var dates: String,
                  var openingHours: String,
                  var weblink: String,
                  var ratings: ArrayList<String>?,
                  var avgAmbience: Float,
                  var avgFood: Float,
                  var avgDrinks: Float,
                  var avgCrowding: Float,
                  var avgFamily: Float,
                  var numberOfRates: Int,
                  var image: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.createDoubleArray(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readArrayList(Market::class.java.classLoader)?.filterIsInstance<String>() as ArrayList<String>,
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readInt(),
            parcel.readString().toString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(address)
        parcel.writeDoubleArray(coordinates)
        parcel.writeString(dates)
        parcel.writeString(openingHours)
        parcel.writeString(weblink)
        parcel.writeList(ratings as List<String>?)
        parcel.writeFloat(avgAmbience)
        parcel.writeFloat(avgFood)
        parcel.writeFloat(avgDrinks)
        parcel.writeFloat(avgCrowding)
        parcel.writeFloat(avgFamily)
        parcel.writeInt(numberOfRates)
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