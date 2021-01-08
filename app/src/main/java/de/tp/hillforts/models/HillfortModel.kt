package de.tp.hillforts.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList

// annote and mark with marker interface to make parcelable
@Parcelize
data class HillfortModel(
    var id: Long = 0L,
    var name: String = "",
    var desc: String = "",
    var loc: LocationModel = LocationModel(),
    var images: List<String> = ArrayList(),
    var visited: Boolean = false,
    var dateVisited: Date? = null,
    var notes: String = ""
): Parcelable {
}