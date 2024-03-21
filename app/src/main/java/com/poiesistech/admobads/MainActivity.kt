package com.poiesistech.admobads

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.poiesistech.admobads.admob_ads.ads_callbacks.BannerAdCallBack
import com.poiesistech.admobads.admob_ads.ads_callbacks.InterstitialAdLoadCallBack
import com.poiesistech.admobads.admob_ads.ads_callbacks.InterstitialAdShowCallBack
import com.poiesistech.admobads.admob_ads.ads_helper.AdmobBannerHelper
import com.poiesistech.admobads.admob_ads.ads_helper.AdmobInterstitialAdHelper
import com.poiesistech.admobads.admob_ads.ads_helper.AdmobNativeHelper
import com.poiesistech.admobads.admob_ads.types.BannerAdTypes
import com.poiesistech.admobads.admob_ads.types.NativeAdTypes
import com.poiesistech.admobads.databinding.ActivityMainBinding
import com.poiesistech.admobads.di.KoinComponents
import com.poiesistech.admobads.remote_config.RemoteConstants
import com.poiesistech.admobads.remote_config.RemoteConstants.remoteCfgValueInterstitialAd
import com.poiesistech.admobads.remote_config.RemoteConstants.remoteCfgValueRemoteCounter
import com.poiesistech.admobads.remote_config.RemoteConstants.totalCount

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val diComponent by lazy { KoinComponents() }
    val admobBanner by lazy { AdmobBannerHelper() }
    val admobNative by lazy { AdmobNativeHelper() }
    val admobInterstitial by lazy { AdmobInterstitialAdHelper() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.mbClickRewarded.setOnClickListener {
            checkCounter()
        }


        admobNative.loadNativeAds(
            this,
            binding.adsNativePlaceHolder,
            getString(R.string.admob_native_ids),
            RemoteConstants.remoteCfgValueNativeAd,
            diComponent.sharedPrefs.isAppPurchased,
            diComponent.internetManager.isInternetAvailable,
            NativeAdTypes.nativeLargeAd,
            object : BannerAdCallBack {
                override fun onAdFailedToLoad(adError: String) {}
                override fun onAdLoaded() {}
                override fun onAdImpression() {}
                override fun onPreloadedAd() {}
                override fun onAdClicked() {}
                override fun onAdClosed() {}
                override fun onAdOpened() {}
            }
        )


        admobBanner.loadBannerAds(
            this,
            binding.adsBannerPlaceHolder,
            getString(R.string.admob_banner_ids),
            RemoteConstants.remoteCfgValueBannerAd,
            diComponent.sharedPrefs.isAppPurchased,
            diComponent.internetManager.isInternetAvailable,
            BannerAdTypes.bottomCollapisbleBannerAd,
            object : BannerAdCallBack {
                override fun onAdFailedToLoad(adError: String) {}
                override fun onAdLoaded() {}
                override fun onAdImpression() {}
                override fun onPreloadedAd() {}
                override fun onAdClicked() {}
                override fun onAdClosed() {
                    // isCollapsibleOpen = false
                }

                override fun onAdOpened() {
                    // isCollapsibleOpen = true
                }


            }
        )
    }


    fun checkCounter() {
        try {
            if (admobInterstitial.isInterstitialLoaded()) {
                showInterstitialAd()
                totalCount += 1
            } else {
                if (totalCount >= remoteCfgValueRemoteCounter) {
                    totalCount = 1
                    loadInterstitialAd()
                } else {
                    totalCount += 1
                }
            }
        } catch (e: Exception) {
            Log.d("AdsInformation", "${e.message}")
        }
    }


    fun loadInterstitialAd() {
        admobInterstitial.loadInterstitialAd(
            this,
            getString(R.string.admob_inter_ids),
            remoteCfgValueInterstitialAd,
            diComponent.sharedPrefs.isAppPurchased,
            diComponent.internetManager.isInternetAvailable,
            object : InterstitialAdLoadCallBack {
                override fun onAdFailedToLoad(adError: String) {}
                override fun onAdLoaded() {}
                override fun onPreloaded() {}
            }
        )
    }

    fun showInterstitialAd() {
        admobInterstitial.showInterstitialAd(
            this,
            object : InterstitialAdShowCallBack {
                override fun onAdDismissedFullScreenContent() {}
                override fun onAdFailedToShowFullScreenContent() {}
                override fun onAdShowedFullScreenContent() {}
                override fun onAdImpression() {}
            }
        )
    }

}