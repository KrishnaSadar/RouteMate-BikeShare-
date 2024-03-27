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
import com.example.driverapp.databinding.ActivityAdharCardBinding
import com.example.driverapp.databinding.ActivityDrivingLicenceBinding
import com.example.driverapp.legalAuthenticationActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class AdharCard : AppCompatActivity() {
    private lateinit var progressDialog: ProgressDialog
    private lateinit var storageRef: StorageReference
    private var imageUrl: String? = null
    private val binding: ActivityAdharCardBinding by lazy{
        ActivityAdharCardBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // Initialize Firebase Storage
        storageRef = FirebaseStorage.getInstance().reference
// Initialize ProgressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading AdharCard...")
        progressDialog.setCancelable(false)
        binding.uploadadhar.setOnClickListener {
            // Open image picker
            openImagePicker()
        }
        binding.back2.setOnClickListener {
            val adharNumber = binding.adharnumber.text.toString().trim()

            if (adharNumber.isEmpty()) {
                MotionToast.createToast(this@AdharCard,
                    "Provide number",
                    "Please provide the Aadhaar number",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this@AdharCard, www.sanju.motiontoast.R.font.montserrat_bold))
                //Toast.makeText(this, "Please provide the Aadhar number", Toast.LENGTH_SHORT).show()
            } else if (!isValidAdharNumber(adharNumber)) {
                MotionToast.createToast(this@AdharCard,
                    "Provide number",
                    "Invalid Aadhar number format",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this@AdharCard, www.sanju.motiontoast.R.font.montserrat_bold))
               // Toast.makeText(this, "Invalid Aadhar number format", Toast.LENGTH_SHORT).show()
            } else {
                if (imageUrl != null) {
                  val  k=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE)
                    val user=k.getString("user","null")
                    val intent = Intent(this, legalAuthenticationActivity::class.java)
                    val editor=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE).edit()
                    editor.putString("boolAdhar","1")
                    editor.putString("AdharNumber",adharNumber)
                    editor.putString("Adharurl",imageUrl.toString()+user.toString())
                    editor.apply()
                    startActivity(intent)
                    finish()
                } else {
                    val d = Intent(this, legalAuthenticationActivity::class.java)
                    d.putExtra("boolAdhar", "0")
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
    // Function to validate Aadhar number format
    private fun isValidAdharNumber(adharNumber: String): Boolean {
        val adharRegex = Regex("\\d{12}")
        return adharRegex.matches(adharNumber)
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
            val imageName = "adhar_img.jpg" // Name for the image file in Firebase Storage
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
                        Glide.with(this).load(it).into(binding.adharpic)
                        progressDialog.dismiss()
                    }
                }
            }.addOnFailureListener { e ->
                // Handle any errors during upload
                progressDialog.dismiss()
                MotionToast.createToast(this@AdharCard,
                    "error encountered",
                    "Upload failed: ${e.message}",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this@AdharCard, www.sanju.motiontoast.R.font.montserrat_bold))
                //Toast.makeText(this, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val IMAGE_PICK_REQUEST = 1
    }
}