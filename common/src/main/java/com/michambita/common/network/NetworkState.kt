package com.michambita.common.network

import kotlinx.coroutines.flow.Flow

interface NetworkState {
    val isOnline: Flow<Boolean>
}
