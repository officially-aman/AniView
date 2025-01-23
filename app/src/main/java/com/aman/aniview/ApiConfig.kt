package com.aman.aniview

object ApiConfig {
    init {
        // Load native library for accessing secure configurations
        System.loadLibrary("secure-config")
    }

    external fun getBaseUrlFromNative(): String

    // Function to fetch the base URL securely
    fun getBaseUrl(): String {
        return getBaseUrlFromNative()
    }
}
