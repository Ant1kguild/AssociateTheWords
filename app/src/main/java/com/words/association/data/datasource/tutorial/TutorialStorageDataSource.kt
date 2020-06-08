package com.words.association.data.datasource.tutorial

import android.content.Context
import androidx.core.content.edit
import com.squareup.moshi.Moshi
import java.lang.reflect.Type

class TutorialStorageDataSource(context: Context, private val moshi: Moshi) : TutorialStorage {
    private val sharedPreferences =
        context.getSharedPreferences(".tutorial", Context.MODE_PRIVATE)


    override fun <T : Any> put(key: String, obj: T) {
        val json: String = obj.toJson(moshi = moshi) ?: return
        sharedPreferences.edit(commit = true) {
            putString(key, json)
        }
    }


    override fun <T> get(key: String, classOfT: Class<T>): T? {
        val json = sharedPreferences.getString(key, null) ?: return null
        return moshi.adapter(classOfT).fromJson(json)
    }

    override fun <T> get(key: String, typeOfT: Type): T? {
        val json = sharedPreferences.getString(key, null) ?: return null
        return moshi.adapter<T>(typeOfT).fromJson(json)
    }

    override fun remove(key: String) {
        sharedPreferences.edit(commit = true) {
            remove(key)
        }
    }

    override fun clear() {
        sharedPreferences.edit(commit = true) {
            clear()
        }
    }

    private fun <T : Any> T.toJson(moshi: Moshi): String? {
        val classOfT = this::class.java
        return moshi.adapter<T>(classOfT).toJson(this)
    }

}