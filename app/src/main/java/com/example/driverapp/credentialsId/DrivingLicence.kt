package com.example.driverapp.credentialsId

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.example.driverapp.R
import com.example.driverapp.databinding.ActivityDrivingLicenceBinding
import com.example.driverapp.legalAuthenticationActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class DrivingLicence : AppCompatActivity() {
    private lateinit var progressDialog: ProgressDialog
    private lateinit var storageRef: StorageReference
    private var imageUrl: String? = null
    private val binding:ActivityDrivingLicenceBinding by lazy{
        ActivityDrivingLicenceBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // Initialize Firebase Storage
        storageRef = FirebaseStorage.getInstance().reference
// Initialize ProgressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading drivingLicence...")
        progressDialog.setCancelable(false)
        binding.drivinglicenceupload.setOnClickListener {
            // Open image picker
            openImagePicker()
        }
        binding.back1.setOnClickListener {
            if(imageUrl!=null){
                val  k=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE)
                val user=k.getString("user","null")
                val intent=Intent(this,legalAuthenticationActivity::class.java)
                val editor=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE).edit()
                editor.putString("booldrivinglicence","1")
                editor.putString("DrvingLicenurl",imageUrl.toString()+user.toString())
                editor.apply()
                startActivity(intent)
                finish()
            }else{
                val d=Intent(this,legalAuthenticationActivity::class.java)
                d.putExtra("booldrivinglicence",0)
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
            val imageName = "driving_license.jpg" // Name for the image file in Firebase Storage
            val imageRef = storageRef.child(imageName)
            val uploadTask = imageRef.putFile(imageUri)
            progressDialog.show()
            uploadTask.addOnSuccessListener { taskSnapshot ->
                // Image uploaded successfully, get the download URL
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    imageUrl = uri.toString()
                    // Set the image URL to ImageView
                    imageUrl?.let {
                        // Load image into ImageView using your preferred image loading library
                        // For example, using Glide:
                     Glide.with(this).load(it).into(binding.panimage)
                        progressDialog.dismiss()
                    }
                }
            }.addOnFailureListener { e ->
                // Handle any errors during upload
                progressDialog.dismiss()
                MotionToast.createToast(this@DrivingLicence,
                    "error encountered",
                    "Upload failed: ${e.message}",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this@DrivingLicence, www.sanju.motiontoast.R.font.montserrat_bold))
                //Toast.makeText(this, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val IMAGE_PICK_REQUEST = 1
    }
}