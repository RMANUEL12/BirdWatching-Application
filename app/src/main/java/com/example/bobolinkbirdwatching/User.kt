package com.example.bobolinkbirdwatching


data class UserData( //Data for account creation
    val id: String?,
    val firstName: String?,
    val surname: String?,
    var area: String?,
    var email: String?,
    var password: String?
)

    object User {
        private val userList = mutableListOf<UserData>()

        fun addUser(userData: UserData) {
            userList.add(userData)
        }

        fun findUserByEmailAndPassword(email: String, password: String): UserData? {
            return userList.find { it.email == email && it.password == password }
        }

    }

