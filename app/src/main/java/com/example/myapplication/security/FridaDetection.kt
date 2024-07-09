package com.example.myapplication.security

import android.app.ActivityManager
import android.content.Context
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

object FridaDetection {
    private val KNOWN_FRIDA_PROCESSES = arrayOf(
        "frida-server",
        "frida-helper",
        "gum-js-loop"
    )
    private val KNOWN_FRIDA_LIBRARIES = arrayOf(
        "libfrida-gadget.so",
        "libfrida-agent.so",
        "libsubstrate.dylib" // Include iOS-specific library
    )

    fun isFridaDetected(context: Context): Boolean {
        return isFridaProcessRunning(context) || isFridaLibraryLoaded()
    }

    private fun isFridaProcessRunning(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            ?: return false // Handle null case for robustness
        for (processInfo in activityManager.runningAppProcesses) {
            if (KNOWN_FRIDA_PROCESSES.contains(processInfo.processName)) {
                return true
            }
        }
        return false
    }

    private fun isFridaLibraryLoaded(): Boolean {
        return try {
            // Check for libraries in common locations (/proc/self/maps, /system/lib)
            for (library in KNOWN_FRIDA_LIBRARIES) {
                if (checkLibraryInMapsFile(library) || checkLibraryInSystemLib(library)) {
                    return true
                }
            }
            false
        } catch (e: Exception) {
            // Handle exceptions gracefully (e.g., log or return false)
            false
        }
    }

    @Throws(Exception::class)
    private fun checkLibraryInMapsFile(library: String): Boolean {
        val mapsFile = File("/proc/self/maps")
        if (!mapsFile.exists()) {
            return false
        }
        BufferedReader(FileReader(mapsFile)).use { reader ->
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                if (line != null && line!!.contains(library)) {
                    return true
                }
            }
        }
        return false
    }

    private fun checkLibraryInSystemLib(library: String): Boolean {
        val libraryFile = File("/system/lib/$library")
        return libraryFile.exists()
    }
}