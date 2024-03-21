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

package com.poiesistech.admobads.di

import com.poiesistech.admobads.internet_connection_manager.InternetManager
import com.poiesistech.admobads.remote_config.RemoteConfiguration
import com.poiesistech.admobads.utils.SharedPreferenceUtils
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class KoinComponents() : KoinComponent {

    val internetManager by inject<InternetManager>()

    val firebaseRemoteConfig by inject<RemoteConfiguration>()

    val sharedPrefs by inject<SharedPreferenceUtils>()

}