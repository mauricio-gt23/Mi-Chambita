package com.michambita.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.michambita.common.network.NetworkState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkStateImpl @Inject constructor(
    @ApplicationContext context: Context
) : NetworkState {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override val isOnline: Flow<Boolean> = callbackFlow {
        val validatedNetworks = mutableSetOf<Network>()

        fun NetworkCapabilities.hasRealInternet(): Boolean =
            hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
                if (capabilities.hasRealInternet()) {
                    validatedNetworks.add(network)
                } else {
                    validatedNetworks.remove(network)
                }
                trySend(validatedNetworks.isNotEmpty())
            }

            override fun onLost(network: Network) {
                validatedNetworks.remove(network)
                trySend(validatedNetworks.isNotEmpty())
            }
        }

        // registerNetworkCallback (API 21+) es compatible con minSdk 23,
        // a diferencia de registerDefaultNetworkCallback (API 24+).
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        // Emitir el estado actual al suscribirse.
        trySend(currentlyOnline())

        connectivityManager.registerNetworkCallback(request, callback)

        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }.distinctUntilChanged()

    private fun currentlyOnline(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}
