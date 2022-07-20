package com.example.notesapplication.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.notesapplication.utils.Constants.PREFS_TOKEN_FILE
import com.example.notesapplication.utils.Constants.USER_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


// Context will be provided by hilt
//ApplicationContext is used throughout the application
//ActivityContext is used only for the specific  activity

class TokenManager @Inject constructor(@ApplicationContext context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_TOKEN_FILE, Context.MODE_PRIVATE)
    //MODE_PRIVATE because it will be used only in this application

    fun saveToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String? {
        return prefs.getString(USER_TOKEN, null)
        //null because token can be null
    }
}