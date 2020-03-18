package com.crazy.instalogin_graphapi.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * @author Pradeepkumar2091
 * Created on 17-03-2020
 */

object InternetUtils {

    fun isInternetConnected(context: Context?): Boolean {
        if (context != null) {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo: NetworkInfo?
            activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
        return false
    }

}
