package de.tp.hillforts.helpers

import de.tp.hillforts.models.user.IUserRepo
import de.tp.hillforts.models.user.UserMemRepo
import de.tp.hillforts.models.user.UserModel

class AuthProvider private constructor(){

    var currentUser: UserModel? = null

    private lateinit var INSTANCE: AuthProvider
    private val users: IUserRepo = UserMemRepo()

    fun getInstance(): AuthProvider{
        if (::INSTANCE.isInitialized){
            return INSTANCE
        }
        return AuthProvider()
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
            users.createUser(email, password)
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
            return user.password.equals(password)
        }
        return false
    }
}