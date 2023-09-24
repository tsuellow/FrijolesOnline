package com.ccy.apps.frijolesonline.data.remote

import com.google.firebase.database.DataSnapshot

class FirebaseParser<T>(val klass: Class<T>) {

    companion object {
        inline operator fun <reified T : Any> invoke() = FirebaseParser(T::class.java)
    }

    private fun checkType(t: Any): Boolean {
        return when {
            klass.isAssignableFrom(t.javaClass) -> true
            else -> false
        }

    }

    fun convert(data: DataSnapshot): T {

        val res = data.getValue() // Get out whatever is there.
            ?: throw FirebaseParserDataNullException("Data was null") // If its null throw exception

        if(checkType(res)) {
            return res as T //typecast is now safe
        } else {
            throw FirebaseParserUncastableException("Data was of wrong type. Expected: " + klass  + " but got: " + res.javaClass) // Data was the wrong type throw exception
        }
    }

}

class FirebaseParserUncastableException(msg: String) : Throwable() {

}

class FirebaseParserDataNullException(msg: String) : Throwable() {

}
