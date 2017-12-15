package com.projects.melih.gistpub

import android.app.Application
import android.os.StrictMode

//import com.crashlytics.android.Crashlytics
//import com.crashlytics.android.core.CrashlyticsCore
//import io.fabric.sdk.android.Fabric

/**
 * Created by melih on 19/02/2017
 */

open class GistPubApplication : Application() {

    override fun onCreate() {
        if (BuildConfig.STRICT_MODE) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build())
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build())
        }

        super.onCreate()

        /*if (BuildConfig.LEAK_CANARY) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                return;
            }
            refWatcher = LeakCanary.install(this);
        }*/

        /* Fabric.with(this, new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build(), new Crashlytics());*/
    }

    /*companion object {
        val refWatcher: RefWatcher? = null
    }*/
}
