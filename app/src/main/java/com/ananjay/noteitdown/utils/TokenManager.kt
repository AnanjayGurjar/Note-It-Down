package com.ananjay.noteitdown.utils

import android.content.Context
import com.ananjay.noteitdown.utils.AppConstants.Companion.APP_SHARED_PREFERENCE
import com.ananjay.noteitdown.utils.AppConstants.Companion.USER_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context: Context) {

    private val prefs = context.getSharedPreferences(APP_SHARED_PREFERENCE, Context.MODE_PRIVATE)

    fun saveToken(token: String){
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String?{
        return prefs.getString(USER_TOKEN, null)
    }

}