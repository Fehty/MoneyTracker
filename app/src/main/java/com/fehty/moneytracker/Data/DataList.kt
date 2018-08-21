package com.fehty.moneytracker.Data

import android.os.Parcel
import android.os.Parcelable

data class DataList(
        var id: Int,
        val name: String,
        val price: String,
        val type: String,
        val date: String
) : Parcelable {

    var _price: String = price
        get() {
            return price + "\u20BD"
        }

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel?, p1: Int) {
        parcel?.writeInt(id)
        parcel?.writeString(name)
        parcel?.writeString(price)
        parcel?.writeString(type)
        parcel?.writeString(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataList> {
        override fun createFromParcel(parcel: Parcel): DataList {
            return DataList(parcel)
        }

        override fun newArray(size: Int): Array<DataList?> {
            return arrayOfNulls(size)
        }
    }


}
