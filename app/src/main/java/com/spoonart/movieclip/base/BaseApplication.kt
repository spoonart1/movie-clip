package com.spoonart.libs.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.orm.SugarContext
import java.util.*


class BaseApplication : Application() {

    private var topActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()
        SugarContext.init(this)
        register()
    }

    override fun onTerminate() {
        super.onTerminate()
        SugarContext.terminate()
    }

    fun setLocaleDefaultID() {
        val locale = Locale("in")
        Locale.setDefault(locale)
    }

    fun register() {
        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
                topActivity = activity
            }

            override fun onActivityResumed(activity: Activity?) {
                topActivity = activity
            }

            override fun onActivityStarted(activity: Activity?) {

            }

            override fun onActivityDestroyed(activity: Activity?) {

            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

            }

            override fun onActivityStopped(activity: Activity?) {

            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {

            }

        })
    }

    companion object {
        private var activityVisible: Boolean = false

        fun isActivityVisible(): Boolean {
            return activityVisible
        }

        fun activityResumed() {
            activityVisible = true
        }

        fun activityPaused() {
            activityVisible = false
        }
    }
}
