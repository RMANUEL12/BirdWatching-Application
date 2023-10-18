package com.example.bobolinkbirdwatching

    data class UserData(val firstName: String, val surname: String,
                     val area: String, val email: String, val password: String)

    object User {
        private val userList = mutableListOf<UserData>()

        fun addUser(userData: UserData) {
            userList.add(userData)
        }

        fun findUserByEmailAndPassword(email: String, password: String): UserData? {
            return userList.find { it.email == email && it.password == password }
        }

    }

