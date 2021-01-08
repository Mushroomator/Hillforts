package de.tp.hillforts.models

import java.util.*
import java.util.concurrent.atomic.AtomicLong


data class HillfortModel(
    var id: Long = 0L,
    var name: String = "",
    var desc: String = "",
    var loc: Location = Location(),
    var visited: Boolean = false,
    var dateVisited: Date? = null,
    var notes: String
) {
}