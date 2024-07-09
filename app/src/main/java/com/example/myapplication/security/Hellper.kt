package com.example.myapplication.security
import java.io.File
import com.stericson.RootTools.RootTools
import com.stericson.RootTools.execution.Command

 object Hellper {
    fun isDeviceRooted() {
        if( checkRootMethod1() || checkRootMethod2() || checkRootMethod3()) {
            println("checkRootMethod1 " + checkRootMethod1().toString());
            println("checkRootMethod2 " + checkRootMethod2().toString());
            println("checkRootMethod3 " + checkRootMethod3().toString());
        }
           // android.os.Process.killProcess(android.os.Process.myPid())
    }

    private fun checkRootMethod1(): Boolean {
        val buildTags = android.os.Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }

    private fun checkRootMethod2(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/tmp/frida-server",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su"
        )
        return paths.any { File( "/data/local/tmp/frida-server").exists() }
    }

    private fun checkRootMethod3(): Boolean {
        var process: Process? = null
        return try {
            process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            val `in` = process.inputStream
            `in`.read() != -1
        } catch (e: Exception) {
            false
        } finally {
            process?.destroy()
        }
    }

     private fun checkRootAccess() {
         if (RootTools.isRootAvailable()) {
             Log.d("RootCheck", "Root is available")
             if (RootTools.isAccessGiven()) {
                 Log.d("RootCheck", "Root access is granted")
                 executeRootCommand()
             } else {
                 Log.d("RootCheck", "Root access is not granted")
             }
         } else {
             Log.d("RootCheck", "Root is not available")
         }
     }
}