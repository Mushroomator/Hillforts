package de.tp.hillforts.models.user

import android.provider.ContactsContract
import de.tp.hillforts.models.hillfort.HillfortModel

interface IUserRepo {

    /**
     * Create a new user and store it.
     * @param email user email address
     * @param password user password
     * @author Thomas Pilz
     * @return created user
     */
    fun createUser(email: String, password: String): UserModel

    /**
     * Delete a user from the repo.
     * @param user user to be deleted
     */
    fun deleteUser(user: UserModel)

    /**
     * Find a hillfort by its ID.
     * @param id ID of a hillfort
     * @author Thomas Pilz
     * @return found hillford or null of not found
     */
    fun findById(id: Long): UserModel?

    /**
     * Get all hillforts stored in the repo.
     * @return all hillforts
     */
    fun findAll(): List<UserModel>
}