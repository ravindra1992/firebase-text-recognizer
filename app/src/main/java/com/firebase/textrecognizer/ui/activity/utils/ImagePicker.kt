package com.firebase.textrecognizer.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri


class ImagePicker(private val activity: Activity) {

    var onUriSelectedListener = { _: Uri -> }

    fun startGalleryActivity() {
        Intent(Intent.ACTION_GET_CONTENT).apply {
            setType("image/*");
            val chooser = Intent.createChooser(this, "Select Picture")
            activity.startActivityForResult(chooser, GALLEY_REQUEST_CODE);
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GALLEY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { onUriSelectedListener(it) }
        }
    }

    companion object {
        private const val GALLEY_REQUEST_CODE = 1001
    }
}