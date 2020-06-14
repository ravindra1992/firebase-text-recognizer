package com.firebase.textrecognizer.ui.activity

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.firebase.textrecognizer.R
import com.firebase.textrecognizer.utils.ImagePicker
import com.firebase.textrecognizer.utils.PermissionUtils
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity() {
    private val imagePicker by lazy { ImagePicker(this) }
    var cameraFile: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }
    private fun init() {
        capture_btn.setOnClickListener {
            showPictureDialog()
        }
        imagePicker.onUriSelectedListener = {
            startActivity(RecognizeActivity.createIntent(this, it))
        }
    }

    private fun showPictureDialog() {

        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle(getString(R.string.select_action))
        val pictureDialogItems =
            arrayOf(getString(R.string.select_photo_from_gallery), getString(R.string.capture_photo_from_camera))
        pictureDialog.setItems(pictureDialogItems,
            DialogInterface.OnClickListener { _, which ->
                when (which) {
                    0 -> startGalleryActivity()
                    1 -> openCamera(getLocalImageFileName())
                }
            })
        pictureDialog.show()
    }

    fun openCamera(fileName: String) {
        if (PermissionUtils.checkPermissions(this)){
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val cameraOutput = getCameraOutput(fileName)
            val outPut = getUriFromFile(cameraOutput)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPut)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivityForResult(intent, RC_PICK_PHOTO_CAMERA)
        }else{
            PermissionUtils.requestPermissions(this, CAMERA_PERMISSION_REQUEST_CODE)

        }
    }

    fun getUriFromFile(cameraOutput: File): Uri {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            return FileProvider.getUriForFile(this, packageName + ".provider", cameraOutput)
        } else {
            return Uri.fromFile(cameraOutput)
        }
    }

    private fun startGalleryActivity() {
        if (PermissionUtils.checkPermissions(this))
            imagePicker.startGalleryActivity()
        else
            PermissionUtils.requestPermissions(this, GALLERY_PERMISSION_REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_PICK_PHOTO_CAMERA ->
                if (resultCode == Activity.RESULT_OK) {
                    val cameraOutput = getCameraOutput(cameraFile)
                    val selectedImageUri = Uri.fromFile(cameraOutput)
                    startActivity(RecognizeActivity.createIntent(this, selectedImageUri))
                }
        }
        imagePicker.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && PermissionUtils.checkPermissions(this))
            openCamera(cameraFile)
        else if (requestCode == GALLERY_PERMISSION_REQUEST_CODE && PermissionUtils.checkPermissions(this))
            imagePicker.startGalleryActivity()
    }
    fun getCameraOutput(fileName: String): File {
        if (fileName.isNullOrEmpty()) {
            Toast.makeText(this, "Invalid file name", Toast.LENGTH_SHORT).show()
        }

        val dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(dir, fileName)
    }

    fun getLocalImageFileName(): String {
        cameraFile = "test-" + UUID.randomUUID().toString() + ".jpeg"
        return cameraFile as String
    }
    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 200
        private const val GALLERY_PERMISSION_REQUEST_CODE = 201
        val RC_PICK_PHOTO_CAMERA: Int = 3

    }

}