/*
 * **************************************************************
 *  * @Author: Tariq Hussain
 *  * @Date: 3/20/24, 2:22 PM.
 *  * @Accounts
 *  *      -> https://github.com/Tariq2518
 *  *      -> https://www.linkedin.com/in/tariqhpk/
 *  *
 *  * All rights reserved.
 *  * Copying and redistributing are not allowed.
 *  **************************************************************
 */

package com.poiesistech.admobads.admob_ads.types

sealed class NativeAdTypes {
    object nativeBannerAd : NativeAdTypes()
    object nativeSmallAd : NativeAdTypes()
    object nativeLargeAd : NativeAdTypes()
    object nativeExLargeAd : NativeAdTypes()
}