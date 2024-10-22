package com.google.devrel.imageclassifierstep1

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create an instance of ImageView using its ID from the layout
        val img: ImageView = findViewById(R.id.imageToLabel)
        val fileName = "flower1.jpg" // Image that will appear
        // Get the bitmap from the assets folder using the helper function
        val bitmap: Bitmap? = assetsToBitmap(fileName)
        // Set the bitmap to the ImageView if it's not null
        bitmap?.apply {
            img.setImageBitmap(this)
        }

        // Get a reference to the TextView where the label will be rendered
        val txtOutput: TextView = findViewById(R.id.txtOutput)
        // Get a reference to the Button using its ID from the layout
        val btn: Button = findViewById(R.id.btnTest)
        // Set a click listener on the button to perform image labeling
        btn.setOnClickListener {
            // Create an ImageLabeler object with default options
            val labeler: ImageLabeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
            // Convert the bitmap to an InputImage for ML Kit
            val image = InputImage.fromBitmap(bitmap!!, 0)
            // Variable to hold output text
            var outputText = ""
            // Process the image and retrieve the labels
            labeler.process(image)
                .addOnSuccessListener { labels ->
                    // Task completed successfully
                    for (label in labels) {
                        // Each label comes with the text and confidence score
                        val text = label.text
                        val confidence = label.confidence
                        outputText += "$text : $confidence\n"
                    }
                    // Update the TextView with the output text
                    txtOutput.text = outputText
                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    txtOutput.text = "Error: ${e.message}"
                }
        }
    }

    // Function to load an image from assets and convert it to a Bitmap
    private fun assetsToBitmap(fileName: String): Bitmap? {
        return try {
            // Open the file from the assets folder
            val inputStream = assets.open(fileName)
            // Decode the InputStream to a Bitmap
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
