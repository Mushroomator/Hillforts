package de.tp.hillforts.helpers

import android.content.Context
import de.tp.hillforts.main.MainApp
import de.tp.hillforts.models.user.IUserRepo
import de.tp.hillforts.models.user.UserJsonRepo
import de.tp.hillforts.models.user.UserMemRepo
import de.tp.hillforts.models.user.UserModel

object AuthProvider {

    var currentUser: UserModel? = null
    lateinit var context: Context
    lateinit var users: IUserRepo

    fun init(context: Context){
        this.context = context
        users = UserJsonRepo(context)
    }

    /**
     * Simple sign up. Checks if email address already exists and if so creates a user.
     * No check if email address is formatted correctly or does exist and no criteria for passwords.
     * @param email new user's desired email address
     * @param password new user's desired password
     * @return true when successfully signed up. false if email is already in use
     */
    fun signUp(email: String, password: String): Boolean{
        val found = users.findByEmail(email)
        if(found == null){
            val newUser = users.createUser(email, password)
            currentUser = newUser
            return true
        }
        return false
    }

    /**
     * Sign in a user.
     * @param email a user's email address
     * @param password a user's password
     * @return true if successfully logged in. false if emai + password combination does not exist
     */
    fun signIn(email: String, password: String): Boolean{
        val user = users.findByEmail(email)
        if (user != null){
            if(user.password.equals(password)){
                currentUser = user
                return true
            }
            return false
        }
        return false
    }

    fun logout(){
        if(currentUser != null){
            currentUser = null
        }
    }
}