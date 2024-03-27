package com.example.driverapp.credentialsId

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.example.driverapp.R
import com.example.driverapp.databinding.ActivityPanCardBinding
import com.example.driverapp.legalAuthenticationActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class PanCard : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog
    private lateinit var storageRef: StorageReference
    private var imageUrl: String? = null
    private val binding: ActivityPanCardBinding by lazy {
        ActivityPanCardBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initialize Firebase Storage
        storageRef = FirebaseStorage.getInstance().reference

        // Initialize ProgressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading PanCard...")
        progressDialog.setCancelable(false)

        binding.panupload.setOnClickListener {
            // Open image picker
            openImagePicker()
        }

        binding.back3.setOnClickListener {
            val panNumber = binding.pannumber.text.toString().trim()

            if (panNumber.isEmpty()) {
                MotionToast.createToast(this@PanCard,
                    "Pan number Required",
                    "Please provide the PAN number",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this@PanCard, www.sanju.motiontoast.R.font.montserrat_bold))
               // Toast.makeText(this, "Please provide the PAN number", Toast.LENGTH_SHORT).show()
            } else if (!isValidPanNumber(panNumber)) {
                MotionToast.createToast(this@PanCard,
                    "Improper format",
                    "Invalid PAN number format",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this@PanCard, www.sanju.motiontoast.R.font.montserrat_bold))
               // Toast.makeText(this, "Invalid PAN number format", Toast.LENGTH_SHORT).show()
            } else {
                if (imageUrl != null) {
                    val  k=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE)
                    val user=k.getString("user","null")
                    val intent = Intent(this, legalAuthenticationActivity::class.java)
                    val editor=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE).edit()
                    editor.putString("boolpan","1")
                    editor.putString("PanNumber",panNumber)
                    editor.putString("Panurl",imageUrl.toString()+user.toString())
                    editor.apply()
                    startActivity(intent)
                    finish()
                } else {
                    val d = Intent(this, legalAuthenticationActivity::class.java)
                    d.putExtra("boolpan", "0")
                    startActivity(d)
                    finish()
                }
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_REQUEST)
    }

    private fun isValidPanNumber(panNumber: String): Boolean {
        val panRegex = Regex("[A-Z]{5}[0-9]{4}[A-Z]{1}")
        return panRegex.matches(panNumber)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data
            uploadImageToFirebase(imageUri)
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri?) {
        if (imageUri != null) {
            val imageName = "pan_img.jpg" // Name for the image file in Firebase Storage
            val imageRef = storageRef.child(imageName)
            val uploadTask = imageRef.putFile(imageUri)

            progressDialog.show()

            uploadTask.addOnSuccessListener { taskSnapshot ->
                // Image uploaded successfully, get the download URL
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    imageUrl = uri.toString()
                    // Set the image URL to ImageView
                    imageUrl?.let {
                        // Load image into ImageView using Glide
                        Glide.with(this).load(it).into(binding.panimage1)
                        progressDialog.dismiss()
                    }
                }
            }.addOnFailureListener { e ->
                // Handle any errors during upload
                progressDialog.dismiss()
                MotionToast.createToast(this@PanCard,
                    "error encountered",
                    "Upload failed: ${e.message}",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this@PanCard, www.sanju.motiontoast.R.font.montserrat_bold))
                //Toast.makeText(this, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val IMAGE_PICK_REQUEST = 1
    }
}
