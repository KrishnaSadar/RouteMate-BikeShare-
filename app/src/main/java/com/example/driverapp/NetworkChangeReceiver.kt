package com.example.driverapp
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class NetworkChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            val isConnected = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
            if (isConnected) {
                // Internet connection is available
                sendConnectionStatus(context, true)
            } else {
                // Internet connection is not available
                sendConnectionStatus(context, false)
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            val isConnected = networkInfo?.isConnected ?: false
            if (isConnected) {
                // Internet connection is available
                sendConnectionStatus(context, true)
            } else {
                // Internet connection is not available
                sendConnectionStatus(context, false)
            }
        }
    }

    private fun sendConnectionStatus(context: Context?, isConnected: Boolean) {
        val intent = Intent("com.example.driverapp.CONNECTIVITY_CHANGE")
        intent.putExtra("isConnected", isConnected)
        context?.sendBroadcast(intent)
    }
}
