package com.vitalii.redditapi.model

import android.os.Parcel
import android.os.Parcelable

data class Post(
    val authorName: String?,
    val timeOfCrate: Long,
    val thumbnail: String?,
    val numberOfComments: Int,
    val image: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun describeContents(): Int {
        return 0;
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeValue(authorName);
        dest?.writeValue(timeOfCrate);
        dest?.writeValue(thumbnail);
        dest?.writeValue(numberOfComments);
        dest?.writeValue(image);
    }

    companion object CREATOR : Parcelable.Creator<Post> {
        override fun createFromParcel(parcel: Parcel): Post {
            return Post(parcel)
        }

        override fun newArray(size: Int): Array<Post?> {
            return arrayOfNulls(size)
        }
    }
}
