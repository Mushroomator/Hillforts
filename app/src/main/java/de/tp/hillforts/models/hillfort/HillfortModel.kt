package de.tp.hillforts.models.hillfort

import android.os.Parcelable
import de.tp.hillforts.models.location.LocationModel
import kotlinx.android.parcel.Parcelize
import java.util.*

// annote and mark with marker interface to make parcelable
@Parcelize
data class HillfortModel(
    var id: Long = 0L,
    var name: String = "",
    var desc: String = "",
    var loc: LocationModel = LocationModel(),
    var images: MutableList<String> = mutableListOf(),
    var dateVisited: Date? = null,
    var notes: String = ""
): Parcelable {
}