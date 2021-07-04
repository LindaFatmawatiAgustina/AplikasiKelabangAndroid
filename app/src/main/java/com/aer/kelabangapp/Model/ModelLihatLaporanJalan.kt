package com.aer.kelabangapp.Model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Dhimas Panji Sastra on
 * Copyright (c)  . All rights reserved.
 * Last modified $file.lastModified
 * Made With ❤ for U
 */
class ModelLihatLaporanJalan(
    var id: Int = 0,
    var namaJalan: String? = null,
    var latitude: String? = null,
    var longitude: String? = null,
    var fileGambar: String? = null,
    var fileGambar2: String? = null,
    var fileGambar3: String? = null,
    var status: String? = null,
    var tanggalLaporan: String? = null,
    var createdAt: String? = null,
    var updatedAt: String? = null,
    var userId: Int = 0
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(namaJalan)
        parcel.writeString(latitude)
        parcel.writeString(longitude)
        parcel.writeString(fileGambar)
        parcel.writeString(fileGambar2)
        parcel.writeString(fileGambar3)
        parcel.writeString(status)
        parcel.writeString(tanggalLaporan)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
        parcel.writeInt(userId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelLihatLaporanJalan> {
        override fun createFromParcel(parcel: Parcel): ModelLihatLaporanJalan {
            return ModelLihatLaporanJalan(parcel)
        }

        override fun newArray(size: Int): Array<ModelLihatLaporanJalan?> {
            return arrayOfNulls(size)
        }
    }
}