/*
 * **************************************************************
 *  * @Author: Tariq Hussain
 *  * @Date: 3/20/24, 1:28 PM.
 *  * @Accounts
 *  *      -> https://github.com/Tariq2518
 *  *      -> https://www.linkedin.com/in/tariqhpk/
 *  *
 *  * All rights reserved.
 *  * Copying and redistributing are not allowed.
 *  **************************************************************
 */

/*
 * **************************************************************
 *  * @Author: Tariq Hussain
 *  * @Date: 3/19/24, 4:07 PM.
 *  * @Accounts
 *  *      -> https://github.com/Tariq2518
 *  *      -> https://www.linkedin.com/in/tariqhpk/
 *  *
 *  * All rights reserved.
 *  * Copying and redistributing are not allowed.
 *  **************************************************************
 */

package com.poiesistech.admobads.admob_ads.ads_constant

import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd

object AdsConstants {
    var admobAppOpenAd: AppOpenAd? = null
    var admobInterstitialAd: InterstitialAd? = null
    var admobPreloadNativeAd: NativeAd? = null
    var isInterstitialLoading = false
    var isNativeAdLoading = false
    var isAppOpenAdLoading = false


    fun destroy() {
        admobAppOpenAd = null
        admobInterstitialAd = null
        admobPreloadNativeAd?.destroy()
        admobPreloadNativeAd = null
        isAppOpenAdLoading = false
        isInterstitialLoading = false
        isNativeAdLoading = false
    }
}