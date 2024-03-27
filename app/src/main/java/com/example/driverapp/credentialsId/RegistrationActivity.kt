package com.example.driverapp.credentialsId

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat

import com.example.driverapp.databinding.ActivityRegistrationBinding
import com.example.driverapp.legalAuthenticationActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class RegistrationActivity : AppCompatActivity() {
    private lateinit var progressDialog: ProgressDialog
    private lateinit var storageRef: StorageReference
    private var imageUrl: String? = null
    private val binding: ActivityRegistrationBinding by lazy {
        ActivityRegistrationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initialize Firebase Storage
        storageRef = FirebaseStorage.getInstance().reference

        // Initialize ProgressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading Registration Certificate...")
        progressDialog.setCancelable(false)

        binding.uploadrc.setOnClickListener {
            // Open image picker
            openImagePicker()
        }

        binding.back6.setOnClickListener {
            val rcNumber = binding.rcnumber.text.toString().trim()

            if (rcNumber.isEmpty()) {
                MotionToast.createToast(this@RegistrationActivity,
                    "RC number required",
                    "Please provide the RC number",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this@RegistrationActivity, www.sanju.motiontoast.R.font.montserrat_bold))
                //Toast.makeText(this, "Please provide the RC number", Toast.LENGTH_SHORT).show()
            } else {
                if (imageUrl != null) {
                    val  k=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE)
                    val user=k.getString("user","null")
                    if (isValidRCNumber(rcNumber)) {
                        val intent = Intent(this, legalAuthenticationActivity::class.java)
                        val editor=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE).edit()
                        editor.putString("kk","1")
                        editor.putString("rcurl",imageUrl.toString()+user.toString())
                        editor.putString("RcNumber",rcNumber.toString())
                        editor.apply()
                        startActivity(intent)
                        finish()
                    } else {
                        MotionToast.createToast(this@RegistrationActivity,
                            "Invalid Format",
                            "Invalid RC number format",
                            MotionToastStyle.WARNING,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this@RegistrationActivity, www.sanju.motiontoast.R.font.montserrat_bold))
                        //Toast.makeText(this, "Invalid RC number format", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val d = Intent(this, legalAuthenticationActivity::class.java)
                    d.putExtra("boolrc", "0")
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

    private fun isValidRCNumber(rcNumber: String): Boolean {
        // Implement your logic to validate RC number format here
        // For example, you can use a regex pattern
        // Modify the regex pattern according to the format of the RC number
        val rcRegex = Regex("[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}")
        return rcRegex.matches(rcNumber)
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
            val imageName = "rc_img.jpg" // Name for the image file in Firebase Storage
            val imageRef = storageRef.child(imageName)
            val uploadTask = imageRef.putFile(imageUri)

            progressDialog.show()

            uploadTask.addOnSuccessListener { taskSnapshot ->
                // Image uploaded successfully, get the download URL
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    imageUrl = uri.toString()
                    // Dismiss progressDialog here
                    progressDialog.dismiss()
                }
            }.addOnFailureListener { e ->
                // Handle any errors during upload
                progressDialog.dismiss()
                MotionToast.createToast(this@RegistrationActivity,
                    "error encountered",
                    "Upload failed: ${e.message}",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this@RegistrationActivity, www.sanju.motiontoast.R.font.montserrat_bold))
                //Toast.makeText(this, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val IMAGE_PICK_REQUEST = 1
    }
}