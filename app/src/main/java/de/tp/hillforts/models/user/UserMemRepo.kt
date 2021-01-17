package de.tp.hillforts.models.user

import de.tp.hillforts.helpers.generateRandomId
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.concurrent.atomic.AtomicLong


class UserMemRepo: IUserRepo, AnkoLogger {

    val users = ArrayList<UserModel>()

    /**
     * Create a new user and store it.
     * @param email user email address
     * @param password user password (will be stored as plain text!!) do not use in production
     * @author Thomas Pilz
     * @return created user
     */
    override fun createUser(email: String, password: String): UserModel? {
        val userFound = findByEmail(email)
        if (userFound == null){
            // user does not yet exist
            val newUser = UserModel(generateRandomId(), email, password)
            users.add(newUser)
            info(users)
            return newUser
        }
        return null
    }

    /**
     * Delete a user from the repo.
     * @param user user to be deleted
     */
    override fun deleteUser(user: UserModel) {
        users.remove(user)
    }

    /**
     * Find a user by its ID.
     * @param id ID of a user
     * @author Thomas Pilz
     * @return found user or null of not found
     */
    override fun findById(id: Long): UserModel? {
        return users.find{ it.id == id }
    }

    /**
     * Find a user by its email address. Case-insensitive.
     * @param email email of a user
     * @author Thomas Pilz
     * @return found user or null of not found
     */
    override fun findByEmail(email: String): UserModel? {
        return users.find{ it.email.equals(email, true) }
    }

    /**
     * Get all users stored in the repo.
     * @return all users
     */
    override fun findAll(): List<UserModel> {
        return users
    }


}