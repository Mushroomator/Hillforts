package de.tp.hillforts.models.user

import android.provider.ContactsContract
import de.tp.hillforts.models.hillfort.HillfortModel
import java.util.*

/**
 * Simple implementation to generate a unique ID for each user.
 * Not really production ready as there might in theory be clashes but with
 * the amount of data handled here its highly unlikely and therefore this approach is used.
 */
fun generateRandomId(): Long {
    return Random().nextLong()
}

interface IUserRepo {

    /**
     * Create a new user and store it.
     * @param email user email address
     * @param password user password
     * @author Thomas Pilz
     * @return created user
     */
    fun createUser(email: String, password: String): UserModel?

    /**
     * Delete a user from the repo.
     * @param user user to be deleted
     */
    fun deleteUser(user: UserModel)

    /**
     * Find a user by its ID.
     * @param id ID of a user
     * @author Thomas Pilz
     * @return found user or null of not found
     */
    fun findById(id: Long): UserModel?

    /**
     * Find a user by its email address.
     * @param email email of a user
     * @author Thomas Pilz
     * @return found user or null of not found
     */
    fun findByEmail(email: String): UserModel?

    /**
     * Get all users stored in the repo.
     * @return all users
     */
    fun findAll(): List<UserModel>
}