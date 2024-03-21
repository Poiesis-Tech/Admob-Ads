/*
 * **************************************************************
 *  * @Author: Tariq Hussain
 *  * @Date: 3/20/24, 2:43 PM.
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
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.poiesistech.admobads.admob_ads.ads_callbacks.InterstitialAdLoadCallBack
import com.poiesistech.admobads.admob_ads.ads_callbacks.InterstitialAdShowCallBack
import com.poiesistech.admobads.admob_ads.ads_constant.AdsConstants.admobInterstitialAd
import com.poiesistech.admobads.admob_ads.ads_constant.AdsConstants.isInterstitialLoading


class AdmobInterstitialAdHelper {

    /**
     * 0 = Don't Show Ads
     * 1 = Show Ads
     */

    fun loadInterstitialAd(
        activity: Activity?,
        interstitialAdId: String,
        isAdEnable: Int,
        isAppPurchased: Boolean,
        isInternetAvailable: Boolean,
        listener: InterstitialAdLoadCallBack
    ) {
        activity?.let { mActivity ->
            if (isInternetAvailable && isAdEnable != 0 && !isAppPurchased && !isInterstitialLoading && interstitialAdId.isNotEmpty()) {
                if (admobInterstitialAd == null) {
                    isInterstitialLoading = true
                    InterstitialAd.load(
                        mActivity,
                        interstitialAdId,
                        AdRequest.Builder().build(),
                        object : InterstitialAdLoadCallback() {
                            override fun onAdFailedToLoad(adError: LoadAdError) {
                                Log.e(
                                    "InterstitialAdTag",
                                    "admob Interstitial onAdFailedToLoad: ${adError.message}"
                                )
                                isInterstitialLoading = false
                                admobInterstitialAd = null
                                listener.onAdFailedToLoad(adError.toString())
                            }

                            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                                Log.d("InterstitialAdTag", "admob Interstitial onAdLoaded")
                                isInterstitialLoading = false
                                admobInterstitialAd = interstitialAd
                                listener.onAdLoaded()
                            }
                        })
                } else {
                    Log.d("InterstitialAdTag", "admob Interstitial onPreloaded")
                    listener.onPreloaded()
                }

            } else {
                Log.e(
                    "InterstitialAdTag",
                    "isAdEnable = $isAdEnable, isAppPurchased = $isAppPurchased, isInternetAvailable = $isInternetAvailable"
                )
                listener.onAdFailedToLoad("isAdEnable = $isAdEnable, isAppPurchased = $isAppPurchased, isInternetAvailable = $isInternetAvailable")
            }
        }
    }

    fun showInterstitialAd(activity: Activity?, listener: InterstitialAdShowCallBack) {
        activity?.let { mActivity ->
            if (admobInterstitialAd != null) {
                admobInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Log.d("InterstitialAdTag", "admob Interstitial onAdDismissedFullScreenContent")
                        listener.onAdDismissedFullScreenContent()
                        admobInterstitialAd = null
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        Log.e(
                            "InterstitialAdTag",
                            "admob Interstitial onAdFailedToShowFullScreenContent: ${adError.message}"
                        )
                        listener.onAdFailedToShowFullScreenContent()
                        admobInterstitialAd = null
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.d("InterstitialAdTag", "admob Interstitial onAdShowedFullScreenContent")
                        listener.onAdShowedFullScreenContent()
                        admobInterstitialAd = null
                    }

                    override fun onAdImpression() {
                        Log.d("InterstitialAdTag", "admob Interstitial onAdImpression")
                        listener.onAdImpression()
                    }
                }
                admobInterstitialAd?.show(mActivity)
            }
        }
    }

    fun showAndLoadInterstitialAd(
        activity: Activity?,
        interstitialAdId: String,
        listener: InterstitialAdShowCallBack
    ) {
        activity?.let { mActivity ->
            if (admobInterstitialAd != null && interstitialAdId.isNotEmpty()) {
                admobInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Log.d("InterstitialAdTag", "admob Interstitial onAdDismissedFullScreenContent")
                        listener.onAdDismissedFullScreenContent()
                        loadAgainInterstitialAd(mActivity, interstitialAdId)
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        Log.e(
                            "InterstitialAdTag",
                            "admob Interstitial onAdFailedToShowFullScreenContent: ${adError.message}"
                        )
                        listener.onAdFailedToShowFullScreenContent()
                        admobInterstitialAd = null
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.d("InterstitialAdTag", "admob Interstitial onAdShowedFullScreenContent")
                        listener.onAdShowedFullScreenContent()
                        admobInterstitialAd = null
                    }

                    override fun onAdImpression() {
                        Log.d("InterstitialAdTag", "admob Interstitial onAdImpression")
                        listener.onAdImpression()
                    }
                }
                admobInterstitialAd?.show(mActivity)
            }
        }
    }

    private fun loadAgainInterstitialAd(
        activity: Activity?,
        interstitialAdId: String
    ) {
        activity?.let { mActivity ->
            if (admobInterstitialAd == null && !isInterstitialLoading) {
                isInterstitialLoading = true
                InterstitialAd.load(
                    mActivity,
                    interstitialAdId,
                    AdRequest.Builder().build(),
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            Log.e("InterstitialAdTag", "admob Interstitial onAdFailedToLoad: $adError")
                            isInterstitialLoading = false
                            admobInterstitialAd = null
                        }

                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            Log.d("InterstitialAdTag", "admob Interstitial onAdLoaded")
                            isInterstitialLoading = false
                            admobInterstitialAd = interstitialAd

                        }
                    })
            }
        }
    }

    fun isInterstitialLoaded(): Boolean {
        return admobInterstitialAd != null
    }

    fun dismissInterstitial() {
        admobInterstitialAd = null
    }

}