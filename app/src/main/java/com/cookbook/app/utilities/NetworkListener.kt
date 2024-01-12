package com.cookbook.app.utilities

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow

@ExperimentalCoroutinesApi
class NetworkListener: ConnectivityManager.NetworkCallback() {
    private val isNetAvailable = MutableStateFlow(false)

    fun checkConnection(context: Context): MutableStateFlow<Boolean> {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(this)

        val isConnected = MutableStateFlow(false)

        val network = connectivityManager.activeNetwork
        Log.d("Network", "active network $network")
        network ?: run {
            isConnected.value = false
            return isConnected
        }

        val actNetwork = connectivityManager.getNetworkCapabilities(network)
        actNetwork ?: run {
            isConnected.value = false
            return isConnected
        }

        isConnected.value = when {
            actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                Log.d("Network", "wifi connected")
                true
            }
            actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                Log.d("Network", "cellular network connected")
                true
            }
            else -> {
                Log.d("Network", "internet not connected")
                false
            }
        }
        isNetAvailable.value =isConnected.value
        return isNetAvailable
    }
//
//    connectivityManager.allNetworks.forEach {
//
//        }
//    }
    override fun onAvailable(network: Network) {
        isNetAvailable.value = true
    }

    override fun onLost(network: Network) {
        isNetAvailable.value = false
    }
}