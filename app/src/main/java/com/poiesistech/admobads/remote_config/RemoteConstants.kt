/*
 * **************************************************************
 *  * @Author: Tariq Hussain
 *  * @Date: 3/20/24, 12:56 PM.
 *  * @Accounts
 *  *      -> https://github.com/Tariq2518
 *  *      -> https://www.linkedin.com/in/tariqhpk/
 *  *
 *  * All rights reserved.
 *  * Copying and redistributing are not allowed.
 *  **************************************************************
 */

package com.poiesistech.admobads.remote_config

object RemoteConstants {

    /**
     * Interstitial Remote Config keys
     */

    const val INTERSTITIAL_AD_RM_KEY = "interstitial_ad"

    /**
     * Interstitial Remote Constants
     *  -> remoteCfgValue:  denotes 'remote configuration values'
     *  -> Note
     *         0:   Ads off
     *         1:   Admob Active
     */

    var remoteCfgValueInterstitialAd: Int = 1

    /**
     * Native Remote Config keys
     */
    const val NATIVE_RM_AD_KEY = "native_ad"

    /**
     * Native Remote Constants
     *  -> remoteCfgValue:  denotes 'remote configuration values'
     *  -> Note
     *         0:   Ads off
     *         1:   Admob Active
     */

    var remoteCfgValueNativeAd: Int = 1


    /**
     * Banner Remote Config keys
     */
    const val BANNER_RM_AD_KEY = "banner_ad"

    /**
     * Banner Remote Constants
     *  -> remoteCfgValue:  denotes 'remote configuration values'
     *  -> Note
     *         0:   Ads off
     *         1:   Collapsible
     *         2:   Adaptive
     */

    var remoteCfgValueBannerAd: Int = 1


    /**
     * AppOpen Remote Config keys
     */
    const val APP_OPEN_RM_KEY = "app_open_ad"

    /**
     * OpenApp Remote Constants
     *  -> remoteCfgValue:  denotes 'remote configuration values'
     *  -> Note
     *         0:   Ads off
     *         1:   Admob Active
     */

    var remoteCfgValueAppOpen: Int = 1

    /**
     * Others Remote Config keys
     */
    const val COUNTER_KEY = "counter"

    /**
     * Others Remote Constants
     */

    var remoteCfgValueRemoteCounter: Int = 3
    var totalCount : Int = 3

}