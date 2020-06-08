package com.words.association.data.datasource.tutorial

import java.lang.reflect.Type

interface TutorialStorage {
    fun <T : Any> put(key: String, obj: T)
    operator fun <T> get(key: String, classOfT: Class<T>): T?
    operator fun <T> get(key: String, typeOfT: Type): T?
    fun remove(key: String)
    fun clear()
}