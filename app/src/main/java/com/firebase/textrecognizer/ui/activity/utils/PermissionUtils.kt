package com.firebase.textrecognizer.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

object PermissionUtils {

    private val permissionList = listOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    )

    fun requestPermissions(activity: Activity, requestCode: Int): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val listToRequest = permissionList.filterNot { isHavePermission(activity, it) }
            if (listToRequest.isEmpty()) return true
            activity.requestPermissions(listToRequest.toTypedArray(), requestCode)
            return false
        }

        return true
    }

    fun checkPermissions(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val listToRequest = permissionList.filterNot { isHavePermission(activity, it) }
            return listToRequest.isEmpty()
        }
        return true
    }

    private fun isHavePermission(context: Context, permission: String) =
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}