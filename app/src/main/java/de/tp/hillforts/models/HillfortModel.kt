package de.tp.hillforts.models

import java.util.*
import java.util.concurrent.atomic.AtomicLong
import kotlin.collections.ArrayList


data class HillfortModel(
    var id: Long = 0L,
    var name: String = "",
    var desc: String = "",
    var loc: Location = Location(),
    var images: List<String> = ArrayList(),
    var visited: Boolean = false,
    var dateVisited: Date? = null,
    var notes: String
) {
}