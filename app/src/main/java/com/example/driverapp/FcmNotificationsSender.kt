package com.example.driverapp
import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class FcmNotificationsSender(
    private val userFcmToken: String,
    private val title: String,
    private val body: String,
    private val mContext: Context,
    private val mActivity: Activity
) {

    private lateinit var requestQueue: RequestQueue
    private val postUrl = "https://fcm.googleapis.com/fcm/send"
    private val fcmServerKey = "FCM SECRETE KEY"

    fun sendNotifications() {
        requestQueue = Volley.newRequestQueue(mActivity)
        val mainObj = JSONObject()
        try {
            mainObj.put("to", userFcmToken)
            val notiObject = JSONObject()
            notiObject.put("title", title)
            notiObject.put("body", body)
            notiObject.put("icon", "logo1") // enter icon that exists in drawable only
            mainObj.put("notification", notiObject)

            val request = object : JsonObjectRequest(
                Method.POST,
                postUrl,
                mainObj,
                Response.Listener<JSONObject> { response ->
                    // Code to run on response
                },
                Response.ErrorListener { error ->
                    // Code to run on error
                    Toast.makeText(
                        mActivity.applicationContext,
                        error.localizedMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val header = HashMap<String, String>()
                    header["content-type"] = "application/json"
                    header["authorization"] = "key=$fcmServerKey"
                    return header
                }
            }
            requestQueue.add(request)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}
