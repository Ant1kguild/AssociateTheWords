package com.words.association.data.datasource

import com.words.association.utils.android.AndroidResourceManager
import io.reactivex.Single
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class EnglishDictionaryDataSource(
    private val androidResourceManager: AndroidResourceManager
) {

    fun getDictionary(): Single<List<String>> = Single.create<List<String>> { emitter ->
        try {
            val stream: InputStream =
                androidResourceManager.getInputStream(FILE)
            val reader = BufferedReader(InputStreamReader(stream))
            val list = mutableListOf<String>()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                line?.let {
                    list.add(it)
                }
            }
            reader.close()
            stream.close()
            if (!emitter.isDisposed) {
                emitter.onSuccess(list)
            }
        } catch (ex: Exception) {
            if (!emitter.isDisposed) {
                emitter.onError(ex)
            }
        }
    }

    companion object {
        const val FILE = "en_vocabulary_3000.txt"
    }
}

