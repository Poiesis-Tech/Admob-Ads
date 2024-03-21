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


package com.poiesistech.admobads.remote_config

import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.poiesistech.admobads.R
import com.poiesistech.admobads.internet_connection_manager.InternetManager
import com.poiesistech.admobads.remote_config.RemoteConstants.APP_OPEN_RM_KEY
import com.poiesistech.admobads.remote_config.RemoteConstants.BANNER_RM_AD_KEY
import com.poiesistech.admobads.remote_config.RemoteConstants.COUNTER_KEY
import com.poiesistech.admobads.remote_config.RemoteConstants.INTERSTITIAL_AD_RM_KEY
import com.poiesistech.admobads.remote_config.RemoteConstants.NATIVE_RM_AD_KEY

class RemoteConfiguration(
    private val internetManager: InternetManager,
    private val sharedPreferences: SharedPreferences
) {

    private val remoteCfgTag = "REMOTE_CONFIG_TG"

    fun checkRemoteConfig(callback: (fetchSuccessfully: Boolean) -> Unit) {
        if (internetManager.isInternetAvailable) {
            val remoteConfig = Firebase.remoteConfig
            val configSettings = com.google.firebase.remoteconfig.remoteConfigSettings {
                minimumFetchIntervalInSeconds = 2
            }
            remoteConfig.setConfigSettingsAsync(configSettings)
            remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
            fetchRemoteValues(callback)
        } else {
            Log.d(remoteCfgTag, "checkRemoteConfig: Internet Not Found!")
            callback.invoke(false)
        }
    }

    private fun fetchRemoteValues(callback: (fetchSuccessfully: Boolean) -> Unit) {
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {
                try {
                    updateRemoteValues(callback)
                } catch (ex: Exception) {
                    Log.d(remoteCfgTag, "fetchRemoteValues: ${it.exception}")
                    callback.invoke(false)
                }
            } else {
                Log.d(remoteCfgTag, "fetchRemoteValues: ${it.exception}")
                callback.invoke(false)
            }
        }.addOnFailureListener {
            Log.d(remoteCfgTag, "fetchRemoteValues: ${it.message}")
            callback.invoke(false)
        }
    }

    @Throws(Exception::class)
    private fun updateRemoteValues(callback: (fetchSuccessfully: Boolean) -> Unit) {
        val remoteConfig = Firebase.remoteConfig

        setPrefRemoteValues(remoteConfig)
        getPrefRemoteValues()
        Log.d(remoteCfgTag, "checkRemoteConfig: Fetched Successfully")
        callback.invoke(true)
    }

    fun getPrefRemoteValues() {
        /**
         * Interstitial
         */
        RemoteConstants.remoteCfgValueInterstitialAd =
            sharedPreferences.getInt(INTERSTITIAL_AD_RM_KEY, 1)

        /**
         * Native
         */
        RemoteConstants.remoteCfgValueNativeAd = sharedPreferences.getInt(NATIVE_RM_AD_KEY, 1)

        /**
         * Banner
         */
        RemoteConstants.remoteCfgValueBannerAd = sharedPreferences.getInt(BANNER_RM_AD_KEY, 1)

        /**
         * OpenApp
         */
        RemoteConstants.remoteCfgValueAppOpen = sharedPreferences.getInt(APP_OPEN_RM_KEY, 1)

        /**
         * Others
         */
        RemoteConstants.remoteCfgValueRemoteCounter = sharedPreferences.getInt(COUNTER_KEY, 3)

        RemoteConstants.totalCount = RemoteConstants.remoteCfgValueRemoteCounter

    }

    @Throws(Exception::class)
    private fun setPrefRemoteValues(remoteConfig: FirebaseRemoteConfig) {
        sharedPreferences.edit().apply {
            /**
             * Interstitial Remote Config
             */
            putInt(INTERSTITIAL_AD_RM_KEY, remoteConfig[INTERSTITIAL_AD_RM_KEY].asLong().toInt())

            /**
             * Native Remote Config
             */
            putInt(NATIVE_RM_AD_KEY, remoteConfig[NATIVE_RM_AD_KEY].asLong().toInt())

            /**
             * Banner Remote Config
             */
            putInt(BANNER_RM_AD_KEY, remoteConfig[BANNER_RM_AD_KEY].asLong().toInt())

            /**
             * OpenApp Remote Config
             */
            putInt(APP_OPEN_RM_KEY, remoteConfig[APP_OPEN_RM_KEY].asLong().toInt())

            /**
             * Others Remote Config
             */
            putInt(COUNTER_KEY, remoteConfig[COUNTER_KEY].asLong().toInt())

            apply()
        }
    }
}