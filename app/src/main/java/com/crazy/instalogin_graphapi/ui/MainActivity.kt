package com.crazy.instalogin_graphapi.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.crazy.instalogin_graphapi.R
import com.crazy.instalogin_graphapi.dialog.InstagramLoginDialog
import com.crazy.instalogin_graphapi.network.ApiClient
import com.crazy.instalogin_graphapi.network.NetworkService
import com.crazy.instalogin_graphapi.utils.InternetUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * @author Pradeepkumar2091
 * Created on 17-03-2020
 */

class MainActivity : AppCompatActivity() {

    private lateinit var appId: String
    private lateinit var appSecret: String
    private lateinit var redirectURI: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        appId = getString(R.string.instagram_app_id)
        appSecret = getString(R.string.instagram_secret)
        redirectURI = getString(R.string.instagram_redirect_uri)


        buttonLogin.setOnClickListener {
            loginInstagram()
        }
    }

    private fun loginInstagram() {

        if (InternetUtils.isInternetConnected(this)) {
            val mAuthUrl = ("https://api.instagram.com/oauth/authorize/"
                    + "?client_id=$appId"
                    + "&redirect_uri=$redirectURI"
                    + "&response_type=code&display=touch&scope=user_profile")

            val dialog = InstagramLoginDialog.newInstance(
                this,
                mAuthUrl,
                object : InstagramLoginDialog.OAuthDialogListener {
                    override fun onComplete(accessToken: String?) {
                        val accessToken1 = accessToken!!.replace("#_", "")
                        getAccessToken(accessToken1)
                    }

                    override fun onError(error: String?) {
                        Toast.makeText(this@MainActivity, error!!, Toast.LENGTH_SHORT).show()
                    }
                })

            dialog.show()
        } else {
            showToast(getString(R.string.msg_no_internet_connection))
        }
    }


    private fun getAccessToken(authCode: String) {
        val call =
            ApiClient.getClient("https://api.instagram.com")?.create(NetworkService::class.java)
        call?.getAccessToken(code = authCode)!!.enqueue(object : Callback<String?> {
            override fun onFailure(call: Call<String?>, t: Throwable) {

                Log.e("TAG", "UserOnFailure ${t.localizedMessage}")
            }

            override fun onResponse(
                call: Call<String?>,
                response: Response<String?>
            ) {
                try {
                    val json = JSONObject(response.body()!!)
                    val accessToken = json.getString("access_token")
                    val userId = json.getString("user_id")

                    Log.d("TAG", "token $accessToken \nuserId $userId")
                    getUserData(accessToken, userId)
                } catch (e: Exception) {
                    showToast(e.localizedMessage)
                    e.printStackTrace()
                }

            }
        })

    }


    private fun getUserData(accessToken: String, userId: String) {

        val call =
            ApiClient.getClient("https://graph.instagram.com/")?.create(NetworkService::class.java)
        call?.me(accessToken)!!.enqueue(object : Callback<String?> {
            override fun onFailure(call: Call<String?>, t: Throwable) {

                Log.e("TAG", "getUserDataOnFailure ${t.localizedMessage}")
            }

            override fun onResponse(
                call: Call<String?>,
                response: Response<String?>
            ) {

                try {
                    val json = JSONObject(response.body()!!)
                    val userName = json.optString("username")

                    val details = "Name: $userName \n User Id: $userId AccessToken: $accessToken"

                    tvDetail.text = details
                } catch (e: Exception) {
                    showToast(e.localizedMessage)
                    e.printStackTrace()
                }
            }
        })

    }

    private fun showToast(msg: String?) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

}
