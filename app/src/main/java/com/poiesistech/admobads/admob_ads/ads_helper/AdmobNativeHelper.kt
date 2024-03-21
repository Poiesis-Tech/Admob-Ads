/*
 * **************************************************************
 *  * @Author: Tariq Hussain
 *  * @Date: 3/20/24, 2:57 PM.
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
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.poiesistech.admobads.R
import com.poiesistech.admobads.admob_ads.ads_callbacks.BannerAdCallBack
import com.poiesistech.admobads.admob_ads.ads_constant.AdsConstants.admobPreloadNativeAd
import com.poiesistech.admobads.admob_ads.types.NativeAdTypes
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdmobNativeHelper {

    /**
     * 0 = Don't Show ads
     * 1 = Show Ads
     */

    private var adMobNativeAd: NativeAd? = null

    /**
     * load native ad and show
     */
    fun loadNativeAds(
        activity: Activity?,
        nativeAdPlaceHolder: FrameLayout,
        nativeId: String,
        isAdEnable: Int,
        isAppPurchased: Boolean,
        isInternetAvailable: Boolean,
        nativeType: NativeAdTypes,
        bannerCallBack: BannerAdCallBack
    ) {
        val handlerException = CoroutineExceptionHandler { _, throwable ->
            Log.e("NativeAdTag", "${throwable.message}")
            bannerCallBack.onAdFailedToLoad("${throwable.message}")
        }
        activity?.let { mActivity ->
            try {
                if (isInternetAvailable && isAdEnable != 0 && !isAppPurchased && nativeId.isNotEmpty()) {
                    nativeAdPlaceHolder.visibility = View.VISIBLE

                    if (admobPreloadNativeAd != null) {
                        adMobNativeAd = admobPreloadNativeAd
                        admobPreloadNativeAd = null
                        Log.d("NativeAdTag", "admob native onAdLoaded")
                        bannerCallBack.onPreloadedAd()
                        displayNativeAd(mActivity, nativeAdPlaceHolder, nativeType)
                        return
                    }
                    if (adMobNativeAd == null) {
                        CoroutineScope(Dispatchers.IO + handlerException).launch {
                            val builder: AdLoader.Builder = AdLoader.Builder(mActivity, nativeId)
                            val adLoader =
                                builder.forNativeAd { unifiedNativeAd: NativeAd? ->
                                    adMobNativeAd = unifiedNativeAd
                                }
                                    .withAdListener(object : AdListener() {
                                        override fun onAdImpression() {
                                            super.onAdImpression()
                                            Log.d("NativeAdTag", "admob native onAdImpression")
                                            bannerCallBack.onAdImpression()
                                            adMobNativeAd = null
                                        }

                                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                                            Log.e("NativeAdTag", "admob native onAdFailedToLoad: ${loadAdError.code}")
                                            bannerCallBack.onAdFailedToLoad(loadAdError.message)
                                            nativeAdPlaceHolder.visibility = View.GONE
                                            adMobNativeAd = null
                                            super.onAdFailedToLoad(loadAdError)
                                        }

                                        override fun onAdLoaded() {
                                            super.onAdLoaded()
                                            Log.d("NativeAdTag", "admob native onAdLoaded")
                                            bannerCallBack.onAdLoaded()
                                            displayNativeAd(mActivity, nativeAdPlaceHolder, nativeType)

                                        }

                                    }).withNativeAdOptions(
                                        com.google.android.gms.ads.nativead.NativeAdOptions.Builder()
                                            .setAdChoicesPlacement(
                                                NativeAdOptions.ADCHOICES_TOP_RIGHT
                                            ).build()
                                    )
                                    .build()
                            adLoader.loadAd(AdRequest.Builder().build())

                        }
                    } else {
                        Log.e("NativeAdTag", "Native is already loaded")
                        bannerCallBack.onPreloadedAd()
                        displayNativeAd(mActivity, nativeAdPlaceHolder, nativeType)
                    }

                } else {
                    nativeAdPlaceHolder.visibility = View.GONE
                    Log.e("NativeAdTag", "isAdEnable = $isAdEnable, isAppPurchased = $isAppPurchased, isInternetAvailable = $isInternetAvailable")
                    bannerCallBack.onAdFailedToLoad("isAdEnable = $isAdEnable, isAppPurchased = $isAppPurchased, isInternetAvailable = $isInternetAvailable")
                }

            } catch (ex: Exception) {
                Log.e("NativeAdTag", "${ex.message}")
                bannerCallBack.onAdFailedToLoad("${ex.message}")
            }
        }
    }

    private fun displayNativeAd(
        activity: Activity?,
        adMobNativeContainer: FrameLayout,
        nativeType: NativeAdTypes,
    ) {
        activity?.let { mActivity ->
            try {
                adMobNativeAd?.let { ad ->
                    val inflater = LayoutInflater.from(mActivity)

                    val adView: NativeAdView = when (nativeType) {
                        NativeAdTypes.nativeBannerAd -> inflater.inflate(R.layout.native_banner, null) as NativeAdView
                        NativeAdTypes.nativeSmallAd -> inflater.inflate(R.layout.native_small, null) as NativeAdView
                        NativeAdTypes.nativeLargeAd -> inflater.inflate(R.layout.native_large, null) as NativeAdView
                        NativeAdTypes.nativeExLargeAd -> if (isSupportFullScreen(mActivity)) {
                            inflater.inflate(R.layout.native_large, null) as NativeAdView
                        } else {
                            inflater.inflate(R.layout.native_small, null) as NativeAdView
                        }
                    }
                    val viewGroup: ViewGroup? = adView.parent as ViewGroup?
                    viewGroup?.removeView(adView)

                    adMobNativeContainer.removeAllViews()
                    adMobNativeContainer.addView(adView)

                    if (nativeType == NativeAdTypes.nativeLargeAd) {
                        val mediaView: MediaView = adView.findViewById(R.id.media_view)
                        adView.mediaView = mediaView
                    }
                    if (nativeType == NativeAdTypes.nativeExLargeAd) {
                        if (isSupportFullScreen(mActivity)) {
                            val mediaView: MediaView = adView.findViewById(R.id.media_view)
                            adView.mediaView = mediaView
                        }
                    }

                    // Set other ad assets.
                    adView.headlineView = adView.findViewById(R.id.ad_headline)
                    adView.bodyView = adView.findViewById(R.id.ad_body)
                    adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
                    adView.iconView = adView.findViewById(R.id.ad_app_icon)

                    //Headline
                    adView.headlineView?.let { headline ->
                        (headline as TextView).text = ad.headline
                        headline.isSelected = true
                    }

                    //Body
                    adView.bodyView?.let { bodyView ->
                        if (ad.body == null) {
                            bodyView.visibility = View.INVISIBLE
                        } else {
                            bodyView.visibility = View.VISIBLE
                            (bodyView as TextView).text = ad.body
                        }
                    }

                    //Call to Action
                    adView.callToActionView?.let { ctaView ->
                        if (ad.callToAction == null) {
                            ctaView.visibility = View.GONE
                        } else {
                            ctaView.visibility = View.VISIBLE
                            (ctaView as Button).text = ad.callToAction
                        }
                    }

                    //Icon
                    adView.iconView?.let { iconView ->
                        if (ad.icon == null) {
                            iconView.visibility = View.GONE
                        } else {
                            (iconView as ImageView).setImageDrawable(ad.icon?.drawable)
                            iconView.visibility = View.VISIBLE
                        }
                    }

                    adView.advertiserView?.let { adverView ->
                        if (ad.advertiser == null) {
                            adverView.visibility = View.GONE
                        } else {
                            (adverView as TextView).text = ad.advertiser
                            adverView.visibility = View.GONE
                        }
                    }

                    adView.setNativeAd(ad)
                }
            } catch (ex: Exception) {
                Log.e("NativeAdTag", "displayNativeAd: ${ex.message}")
            }
        }
    }

    private fun isSupportFullScreen(activity: Activity): Boolean {
        try {
            val outMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(outMetrics)
            if (outMetrics.heightPixels > 1280) {
                return true
            }
        } catch (ignored: Exception) {
        }
        return false
    }
}