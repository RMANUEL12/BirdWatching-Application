package com.example.bobolinkbirdwatching

import android.content.Context
import android.content.SharedPreferences

//Class to track logged user with sessions
class UserSession(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE)

    var currentEmail: String?
        get() = sharedPreferences.getString("email", null)
        set(value) {
            sharedPreferences.edit().putString("email", value).apply()
        }

    var userID: String?
        get() = sharedPreferences.getString("userId", null)
        set(value) {
            sharedPreferences.edit().putString("userId", value).apply()
        }

    var currentName: String?
        get() = sharedPreferences.getString("userName", null)
        set(value) {
            sharedPreferences.edit().putString("userName", value).apply()
        }

    var currentSurname: String?
        get() = sharedPreferences.getString("userSurname", null)
        set(value) {
            sharedPreferences.edit().putString("userSurname", value).apply()
        }

    var currentArea: String?
        get() = sharedPreferences.getString("userArea", null)
        set(value) {
            sharedPreferences.edit().putString("userArea", value).apply()
        }

    var userLocation: String?
        get() = sharedPreferences.getString("userLocation", null)
        set(value) {
            sharedPreferences.edit().putString("userLocation", value).apply()
        }

}