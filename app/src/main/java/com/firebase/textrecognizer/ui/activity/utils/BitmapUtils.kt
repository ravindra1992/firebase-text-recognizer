package com.firebase.textrecognizer.ui.activity.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore

object BitmapUtils {
    fun loadRotatedBitmap(uri: Uri, activity: Activity): Bitmap {
        val originImage = MediaStore.Images.Media.getBitmap(activity.contentResolver, uri)
        activity.contentResolver.openInputStream(uri)?.let {
            val exifInterface = ExifInterface(it)
            val matrix = Matrix().apply { postRotate(exifInterface.rotationDegrees.toFloat()) }
            return Bitmap.createBitmap(
                originImage,
                0,
                0,
                originImage.getWidth(),
                originImage.getHeight(),
                matrix,
                true
            )
        }
        return originImage
    }
}