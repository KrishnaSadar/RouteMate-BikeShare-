package com.example.driverapp.credentialsId

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.example.driverapp.R
import com.example.driverapp.databinding.ActivityProfilePhotoBinding
import com.example.driverapp.legalAuthenticationActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class ProfilePhoto : AppCompatActivity() {
    private val binding:ActivityProfilePhotoBinding by lazy {
        ActivityProfilePhotoBinding.inflate(layoutInflater)
    }
    private lateinit var progressDialog: ProgressDialog
    private lateinit var storageRef: StorageReference
    private var imageUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // Initialize Firebase Storage
        storageRef = FirebaseStorage.getInstance().reference

        // Initialize ProgressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading ProfilePhoto...")
        progressDialog.setCancelable(false)

        binding.profileupload.setOnClickListener {
            // Open image picker
            openImagePicker()
        }

        binding.back4.setOnClickListener {
                if (imageUrl != null) {
                    val  k=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE)
                    val user=k.getString("user","null")
                    val intent = Intent(this, legalAuthenticationActivity::class.java)
                    val editor=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE).edit()
                    editor.putString("boolprofille","1")
                    editor.putString("Profileurl",imageUrl.toString())
                    editor.apply()
                    startActivity(intent)
                    finish()
                } else {
                    val d = Intent(this, legalAuthenticationActivity::class.java)
                    d.putExtra("boolprofille", "0")
                    startActivity(d)
                    finish()
                }

        }
    }
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_REQUEST)
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
             val  k=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE)
                    val user=k.getString("user","null")
            val imageName = "profile_img"+"user.toString()"+".jpg" // Name for the image file in Firebase Storage
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
                        Glide.with(this).load(it).into(binding.profileimage)
                        progressDialog.dismiss()
                    }
                }
            }.addOnFailureListener { e ->
                // Handle any errors during upload
                progressDialog.dismiss()
                MotionToast.createToast(this@ProfilePhoto,
                    "error encountered",
                    "Upload failed: ${e.message}",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this@ProfilePhoto, www.sanju.motiontoast.R.font.montserrat_bold))
                //Toast.makeText(this, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val IMAGE_PICK_REQUEST = 1
    }
}
