package com.crazy.instalogin_graphapi.network


import com.crazy.instalogin_graphapi.base.AppController
import com.crazy.instalogin_graphapi.utils.InternetUtils
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @author Pradeepkumar2091
 * Created on 17-03-2020
 */

class ConnectivityInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!InternetUtils.isInternetConnected(AppController.appController)) {
            throw NoConnectivityException()
        }

        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
}
