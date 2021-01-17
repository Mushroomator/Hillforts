package de.tp.hillforts.helpers

import java.util.*

/**
 * Simple implementation to generate a unique ID for each user.
 * Not really production ready as there might in theory be clashes but with
 * the amount of data handled here its highly unlikely and therefore this approach is used.
 *  @return random ID
 */
fun generateRandomId(): Long {
    return Random().nextLong()
}