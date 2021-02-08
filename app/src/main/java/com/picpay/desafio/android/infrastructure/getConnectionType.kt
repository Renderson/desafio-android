package com.picpay.desafio.android.infrastructure

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

fun getConnectionType(context: Context): Boolean? {
    var result: Boolean? = false

    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        cm?.run {
            cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                result = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        true
                    }
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        true
                    }
                    else -> {
                        return false
                    }
                }
            }
        }
    } else {
        cm?.run {
            cm.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> {
                        true
                    }
                    ConnectivityManager.TYPE_MOBILE -> {
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }
    }
    return result
}