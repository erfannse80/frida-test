package com.example.myapplication.security

import android.os.Process
import android.content.Context
import java.io.InputStreamReader

import java.io.BufferedReader

import java.io.File

object SecurityChecks {
    fun runSecurityChecks(context: Context) {
        if (FridaDetection.isFridaDetected(context) ||  DebuggerDetection.checkTracerPid()) {
            // Exit the application or take appropriate action
            Process.killProcess(Process.myPid())
        }
    }
}

