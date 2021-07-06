package dev.rohman.livediscussion.models

data class Base<T>(val status: Boolean, val message: String, val data: T)