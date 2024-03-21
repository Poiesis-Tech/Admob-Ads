/*
 * **************************************************************
 *  * @Author: Tariq Hussain
 *  * @Date: 3/20/24, 2:35 PM.
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
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.poiesistech.admobads.admob_ads.ads_callbacks.BannerAdCallBack
import com.poiesistech.admobads.admob_ads.types.BannerAdTypes

class AdmobBannerHelper {

    private var bannerAdView: AdView? = null

    /**
     * 0 = Don't Show Ads
     * 1 = Show Collapsible Banner
     * 2 = Show Adaptive Banner
     */

    fun loadBannerAds(
        activity: Activity?,
        bannerAdPlaceHolder: FrameLayout,
        bannerAdId: String,
        isAdEnable: Int,
        isAppPurchased: Boolean,
        isInternetConnected: Boolean,
        bannerAdType: BannerAdTypes = BannerAdTypes.adaptiveBannerAd,
        bannerAdCallBack: BannerAdCallBack
    ) {
        activity?.let { mActivity ->
            try {
                if (isInternetConnected && isAdEnable != 0 && !isAppPurchased && bannerAdId.isNotEmpty()) {
                    bannerAdPlaceHolder.visibility = View.VISIBLE
                    bannerAdView = AdView(mActivity)
                    bannerAdView?.adUnitId = bannerAdId
                    try {
                        bannerAdView?.setAdSize(getAdSize(mActivity, bannerAdPlaceHolder))
                    } catch (ex: Exception) {
                        bannerAdView?.setAdSize(AdSize.BANNER)
                    }

                    val adRequest: AdRequest = when (isAdEnable) {
                        1 -> when (bannerAdType) {
                            BannerAdTypes.adaptiveBannerAd -> {
                                AdRequest
                                    .Builder()
                                    .build()
                            }

                            BannerAdTypes.bottomCollapisbleBannerAd -> {
                                AdRequest
                                    .Builder()
                                    .addNetworkExtrasBundle(
                                        AdMobAdapter::class.java,
                                        Bundle().apply {
                                            putString("collapsible", "bottom")
                                        })
                                    .build()
                            }

                            BannerAdTypes.topCollapisbleBannerAd -> {
                                AdRequest
                                    .Builder()
                                    .addNetworkExtrasBundle(
                                        AdMobAdapter::class.java,
                                        Bundle().apply {
                                            putString("collapsible", "top")
                                        })
                                    .build()
                            }
                        }

                        else -> AdRequest
                            .Builder()
                            .build()
                    }

                    bannerAdView?.loadAd(adRequest)
                    bannerAdView?.adListener = object : AdListener() {
                        override fun onAdLoaded() {
                            Log.d("BannerAdTag", "admob banner onAdLoaded")
                            showBannerAd(bannerAdPlaceHolder)
                            bannerAdCallBack.onAdLoaded()
                        }

                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            Log.e(
                                "BannerAdTag",
                                "admob banner onAdFailedToLoad: ${adError.message}"
                            )
                            bannerAdPlaceHolder.visibility = View.GONE
                            bannerAdCallBack.onAdFailedToLoad(adError.message)
                        }

                        override fun onAdImpression() {
                            Log.d("BannerAdTag", "admob banner onAdImpression")
                            bannerAdCallBack.onAdImpression()
                            super.onAdImpression()
                        }

                        override fun onAdClicked() {
                            Log.d("BannerAdTag", "admob banner onAdClicked")
                            bannerAdCallBack.onAdClicked()
                            super.onAdClicked()
                        }

                        override fun onAdClosed() {
                            Log.d("BannerAdTag", "admob banner onAdClosed")
                            bannerAdCallBack.onAdClosed()
                            super.onAdClosed()
                        }

                        override fun onAdOpened() {
                            Log.d("BannerAdTag", "admob banner onAdOpened")
                            bannerAdCallBack.onAdOpened()
                            super.onAdOpened()
                        }
                    }
                } else {
                    bannerAdPlaceHolder.removeAllViews()
                    bannerAdPlaceHolder.visibility = View.GONE
                    Log.e(
                        "BannerAdTag",
                        "isAdEnable = $isAdEnable, isAppPurchased = $isAppPurchased, isInternetConnected = $isInternetConnected"
                    )
                    bannerAdCallBack.onAdFailedToLoad("isAdEnable = $isAdEnable, isAppPurchased = $isAppPurchased, isInternetConnected = $isInternetConnected")
                }
            } catch (ex: Exception) {
                Log.e("BannerAdTag", "${ex.message}")
                bannerAdCallBack.onAdFailedToLoad("${ex.message}")
            }
        }

    }

    private fun showBannerAd(bannerAdPlaceHolder: FrameLayout) {
        try {
            if (bannerAdView != null) {
                val viewGroup: ViewGroup? = bannerAdView?.parent as? ViewGroup?
                viewGroup?.removeView(bannerAdView)

                bannerAdPlaceHolder.removeAllViews()
                bannerAdPlaceHolder.addView(bannerAdView)
            } else {
                bannerAdPlaceHolder.removeAllViews()
                bannerAdPlaceHolder.visibility = View.GONE
            }
        } catch (ex: Exception) {
            Log.e("BannerAdTag", "inflateBannerAd: ${ex.message}")
        }

    }

    fun admobBannerOnResume() {
        try {
            bannerAdView?.resume()
        } catch (ex: Exception) {
            Log.e("BannerAdTag", "admobBannerOnPause: ${ex.message}")
        }
    }

    fun admobBannerOnPause() {
        try {
            bannerAdView?.pause()
        } catch (ex: Exception) {
            Log.e("BannerAdTag", "admobBannerOnPause: ${ex.message}")
        }

    }

    fun admobBannerOnDestroy() {
        try {
            bannerAdView?.destroy()
            bannerAdView = null
        } catch (ex: Exception) {
            Log.e("BannerAdTag", "admobBannerOnPause: ${ex.message}")
        }
    }

    private fun getAdSize(mActivity: Activity, adContainer: FrameLayout): AdSize {
        val display = mActivity.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val density = outMetrics.density

        var adWidthPixels = adContainer.width.toFloat()
        if (adWidthPixels == 0f) {
            adWidthPixels = outMetrics.widthPixels.toFloat()
        }

        val adWidth = (adWidthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(mActivity, adWidth)
    }

}