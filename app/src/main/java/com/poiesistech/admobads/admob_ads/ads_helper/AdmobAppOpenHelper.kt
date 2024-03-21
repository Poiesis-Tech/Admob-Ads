/*
 * **************************************************************
 *  * @Author: Tariq Hussain
 *  * @Date: 3/20/24, 2:24 PM.
 *  * @Accounts
 *  *      -> https://github.com/Tariq2518
 *  *      -> https://www.linkedin.com/in/tariqhpk/
 *  *
 *  * All rights reserved.
 *  * Copying and redistributing are not allowed.
 *  **************************************************************
 */

package com.poiesistech.admobads.admob_ads.ads_helper

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.poiesistech.admobads.R
import com.poiesistech.admobads.admob_ads.ads_constant.AdsConstants.admobAppOpenAd
import com.poiesistech.admobads.admob_ads.ads_constant.AdsConstants.isAppOpenAdLoading
import com.poiesistech.admobads.di.KoinComponents
import com.poiesistech.admobads.remote_config.RemoteConstants.remoteCfgValueAppOpen
import java.util.Date

class AdmobAppOpenHelper(private val myApplication: Application) : LifecycleObserver,
    Application.ActivityLifecycleCallbacks {
    private var currentActivity: Activity? = null
    private var loadTime: Long = 0
    protected val diComponent by lazy { KoinComponents() }

    /**
     * 0 = Don't Show Ads
     * 1 = Show Ads
     */

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        try {
            if (!diComponent.sharedPrefs.isAppPurchased && remoteCfgValueAppOpen != 0) {
                showAdIfAvailable()
            }
        } catch (ignored: Exception) {
        }
    }

    fun fetchAd() {
        if (isAdAvailable) {
            return
        }
        val loadCallback: AppOpenAd.AppOpenAdLoadCallback =
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(appOpenAd: AppOpenAd) {
                    super.onAdLoaded(appOpenAd)
                    isAppOpenAdLoading = false
                    admobAppOpenAd = appOpenAd
                    Log.d("AppOpenAdTag", "open is loaded")

                    admobAppOpenAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                admobAppOpenAd = null
                                isShowingAd = false
                                fetchAd()
                                if (appOpenListener != null) {
                                    appOpenListener?.onOpenAdClosed()
                                }
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                if (appOpenListener != null) {
                                    appOpenListener?.onOpenAdClosed()
                                    Log.d("AppOpenAdTag", "open is FailedToShow")
                                }
                            }

                            override fun onAdShowedFullScreenContent() {
                                isShowingAd = true
                            }
                        }
                    loadTime = Date().time
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    Log.d("AppOpenAdTag", "open Ad is FailedToLoad")
                    isAppOpenAdLoading = false
                    admobAppOpenAd = null
                }
            }

        if (!diComponent.sharedPrefs.isAppPurchased && remoteCfgValueAppOpen != 0) {
            if (admobAppOpenAd == null && !isAppOpenAdLoading) {
                isAppOpenAdLoading = true
                try {
                    AppOpenAd.load(
                        myApplication,
                        myApplication.getString(R.string.admob_app_open_ids),
                        AdRequest.Builder().build(),
                        loadCallback
                    )
                } catch (ignored: Exception) {
                }
            }
        }
    }

    private fun showAdIfAvailable() {
        try {
            if (!diComponent.sharedPrefs.isAppPurchased && remoteCfgValueAppOpen != 0) {
                if (currentActivity is AdActivity)
                    return

                admobAppOpenAd?.show(currentActivity!!)
            } else {
                fetchAd()
            }
        } catch (ignored: Exception) {
        }
    }

    private var appOpenListener: AppOpenListener? = null

    interface AppOpenListener {
        fun onOpenAdClosed()
    }

    private fun wasLoadTimeLessThanNHoursAgo(): Boolean {
        val dateDifference = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * 4.toLong()
    }

    private val isAdAvailable: Boolean
        get() = admobAppOpenAd != null && wasLoadTimeLessThanNHoursAgo()

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityStopped(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        currentActivity = null
    }

    companion object {
        private var isShowingAd = false
    }

    init {
        this.myApplication.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

}