package com.crazy.instalogin_graphapi.network


import com.crazy.instalogin_graphapi.R
import com.crazy.instalogin_graphapi.base.AppController
import retrofit2.Call
import retrofit2.http.*


/**
 * @author Pradeepkumar2091
 * Created on 17-03-2020
 */

interface NetworkService {

    @FormUrlEncoded
    @POST("oauth/access_token")
    fun getAccessToken(
        @Field("client_id") client_id: String = AppController.appController!!.getString(R.string.instagram_app_id),
        @Field("client_secret") client_secret: String = AppController.appController!!.getString(R.string.instagram_secret),
        @Field("redirect_uri") redirect_uri: String = AppController.appController!!.getString(R.string.instagram_redirect_uri),
        @Field("code") code: String,
        @Field("grant_type") scope: String = "authorization_code"
    ): Call<String>


    @GET("me?fields=id,username")
    fun me(
        @Query("access_token") access_token: String
    ): Call<String>
}
