package com.frontlinehelper.app.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecureStore(private val context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "secure_store",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveCredential(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun getCredential(key: String): String? = prefs.getString(key, null)

    fun clearCredential(key: String) { prefs.edit().remove(key).apply() }
}
