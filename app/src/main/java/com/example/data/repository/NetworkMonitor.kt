package com.example.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

enum class NetworkType {
    WIFI,
    CELLULAR,
    OTHER,
    OFFLINE
}

class NetworkMonitor(context: Context) {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    val networkTypeFlow: Flow<NetworkType> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(getCurrentNetworkType())
            }

            override fun onLost(network: Network) {
                trySend(getCurrentNetworkType())
            }

            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                trySend(getCurrentNetworkType())
            }
        }

        val request = NetworkRequest.Builder().build()
        connectivityManager?.registerNetworkCallback(request, callback)
        trySend(getCurrentNetworkType())

        awaitClose {
            connectivityManager?.unregisterNetworkCallback(callback)
        }
    }

    fun getCurrentNetworkType(): NetworkType {
        val cm = connectivityManager ?: return NetworkType.OFFLINE
        val activeNetwork = cm.activeNetwork ?: return NetworkType.OFFLINE
        val capabilities = cm.getNetworkCapabilities(activeNetwork) ?: return NetworkType.OFFLINE

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.CELLULAR
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) -> NetworkType.OTHER
            else -> NetworkType.OFFLINE
        }
    }

    fun isOnline(): Boolean {
        return getCurrentNetworkType() != NetworkType.OFFLINE
    }
}
