package de.tp.hillforts.models.user

data class UserModel(
    var id: Long = 0L,
    var email: String = "",
    var password: String = "",
)

