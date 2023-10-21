package com.weatheriza.core.entities

data class ErrorEntity(val message: String) {
    companion object {
        fun empty() = ErrorEntity("")
    }
}
