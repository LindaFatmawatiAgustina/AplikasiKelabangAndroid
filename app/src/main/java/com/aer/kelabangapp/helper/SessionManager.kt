package com.aer.kelabangapp.helper

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Dhimas Panji Sastra on
 * Copyright (c)  . All rights reserved.
 * Last modified $file.lastModified
 * Made With ❤ for U
 */

class SessionManager(val context: Context) {
    private val PREFS_NAME = "KelabangAPP"
    val KEY_ID = "keyid"
    val KEY_LOGIN = "islogin"
    val KEY_NAMA = "keynama"
    val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveLogin(id: Int, nama: String, login: Boolean) {

        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putInt(KEY_ID, id)
        editor.putBoolean(KEY_LOGIN, login)
        editor.putString(KEY_NAMA, nama)

        editor.apply()
    }


    fun getId(): Int? {
        return sharedPref.getInt(KEY_ID, 0)
    }

    fun getNama(): String? {
        return sharedPref.getString(KEY_NAMA, null)
    }

    fun isLogin(): Boolean {

        return sharedPref.getBoolean(KEY_LOGIN, false)

    }

    fun logout() {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putInt(KEY_ID, 0)
        editor.putBoolean(KEY_LOGIN, false)
        editor.putString(KEY_NAMA, null)

        editor.apply()


    }

    fun clearSharedPreference() {

        val editor: SharedPreferences.Editor = sharedPref.edit()

        //sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        editor.clear()
        editor.apply()
    }

    fun removeValue(KEY_NAME: String) {

        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.remove(KEY_NAME)
        editor.commit()
    }
}