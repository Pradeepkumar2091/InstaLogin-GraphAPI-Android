package com.crazy.instalogin_graphapi.network


import com.crazy.instalogin_graphapi.R
import com.crazy.instalogin_graphapi.base.AppController
import java.io.IOException

/**
 * @author Pradeepkumar2091
 * Created on 17-03-2020
 */

class NoConnectivityException : IOException() {


    override fun getLocalizedMessage(): String {
        return AppController.appController?.getString(R.string.msg_no_internet_connection)!!
    }

}
