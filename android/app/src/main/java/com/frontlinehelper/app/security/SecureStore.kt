package com.frontlinehelper.app.security

import android.content.Context

class SecureStore(private val context: Context) {
    // Placeholder using SharedPreferences; replace with EncryptedSharedPreferences in production
    private val prefs = context.getSharedPreferences("secure_store", Context.MODE_PRIVATE)

    fun saveCredential(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun getCredential(key: String): String? = prefs.getString(key, null)

    fun clearCredential(key: String) { prefs.edit().remove(key).apply() }
}
