package com.firebase.textrecognizer.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.firebase.textrecognizer.R
import com.firebase.textrecognizer.ui.activity.utils.BitmapUtils
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import java.io.IOException

class RecognizeActivity : AppCompatActivity() {
    private var originImage: Bitmap? = null
    private var textView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recognize)
        init()
    }
    private fun init() {
        val uri = intent.getParcelableExtra<Uri>(URI_EXTRA)
        originImage = BitmapUtils.loadRotatedBitmap(uri, this)
        //detectImage(originImage)
        textView = findViewById(R.id.textView) as TextView
        val imageView = findViewById(R.id.imageView) as ImageView
        imageView.setImageBitmap(originImage)
        detectTextOnDevice(uri)
    }
    private fun detectTextOnDevice(uri: Uri?) {
        if (uri == null) {
        } else {
            // 1
            try {
                val image = FirebaseVisionImage.fromFilePath(this, uri)
                val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
                val result = detector.processImage(image)
                    .addOnSuccessListener { firebaseVisionText ->
                        // Task completed successfully
                        textView?.text = firebaseVisionText.text
                    }
                    .addOnFailureListener { e ->
                        // Task failed with an exception
                        textView?.text = e.toString()
                    }
            }
            catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    companion object {
        private const val URI_EXTRA = "URI_EXTRA"

        fun createIntent(context: Context, uri: Uri) = Intent(context, RecognizeActivity::class.java).apply {
            putExtra(URI_EXTRA, uri)
        }
    }
}