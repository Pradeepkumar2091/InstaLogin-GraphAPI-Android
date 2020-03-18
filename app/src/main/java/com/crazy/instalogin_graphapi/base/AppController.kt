package com.crazy.instalogin_graphapi.base

import android.app.Application

/**
 * @author Pradeepkumar2091
 * Created on 17-03-2020
 */

class AppController : Application() {


    public companion object {
        public var appController: AppController? = null
    }


    override fun onCreate() {
        super.onCreate()
        appController = this
    }

}