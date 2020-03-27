package com.daimler.mbrealmkit.sample

import android.app.Application
import com.daimler.mbloggerkit.MBLoggerKit
import com.daimler.mbloggerkit.PrinterConfig
import com.daimler.mbloggerkit.adapter.AndroidLogAdapter
import com.daimler.mbrealmkit.MBRealmKit
import com.daimler.mbrealmkit.RealmServiceConfig

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        MBLoggerKit.usePrinterConfig(PrinterConfig.Builder()
                .addAdapter(AndroidLogAdapter.Builder()
                        .setLoggingEnabled(BuildConfig.DEBUG)
                        .build())
                .build())

        MBRealmKit.apply {
            createRealmInstance(RealmProviderManager.PROVIDER_ID,
                    RealmServiceConfig.Builder(this@SampleApplication, 1L, RealmModule())
                            .build())
            createRealmInstance(RealmProviderManager.ENCRYPTED_PROVIDER_ID,
                    RealmServiceConfig.Builder(this@SampleApplication, 1L, RealmModule())
                            .encrypt()
                            .build())
        }
    }
}