package com.example.myapplication.security

import android.os.Debug
import android.os.Process
import java.io.File
import java.io.InputStreamReader

object DebuggerDetection {
    fun isBeingTraced(): Boolean {
        return Debug.isDebuggerConnected() || Debug.waitingForDebugger()
    }

    fun checkTracerPid(): Boolean {
        val statusFile = File("/proc/${Process.myPid()}/status")
        if (!statusFile.exists()) return false

        val reader = InputStreamReader(statusFile.inputStream())
        reader.use {
            it.readLines().forEach { line ->
                if (line.startsWith("TracerPid:")) {
                    val tracerPid = line.split("\t")[1].toInt()
                    return tracerPid > 0
                }
            }
        }
        return false
    }
}