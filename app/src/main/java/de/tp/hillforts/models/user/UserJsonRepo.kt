package de.tp.hillforts.models.user

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import de.tp.hillforts.helpers.exists
import de.tp.hillforts.helpers.read
import de.tp.hillforts.helpers.write
import de.tp.hillforts.models.hillfort.HillfortModel
import org.jetbrains.anko.info
import java.util.ArrayList

val JSON_FILE = "users.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<ArrayList<UserModel>>() {}.type

class UserJsonRepo: IUserRepo {

    private val context: Context
    var users = mutableListOf<UserModel>()

    constructor(context: Context){
        this.context = context
        if(exists(context, JSON_FILE)){
            deserialize()
        }
    }

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
            serialize()
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
        serialize()
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

    private fun serialize() {
        val jsonString = de.tp.hillforts.models.hillfort.gsonBuilder.toJson(users, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        users = Gson().fromJson(jsonString, listType)
    }
}