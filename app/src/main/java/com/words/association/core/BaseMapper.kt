package com.words.association.core

interface BaseMapper<in A : Any, out B : Any> {
    fun map(from: A): B
}