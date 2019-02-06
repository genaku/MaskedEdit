package com.example.maskededit

import android.app.Application
import org.mym.plog.DebugPrinter
import org.mym.plog.PLog
import org.mym.plog.config.PLogConfig

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        initPLog()
    }

    private fun initPLog() {
        PLog.init(
            PLogConfig.Builder()
                .forceConcatGlobalTag(true)
                .needLineNumber(true)
                .useAutoTag(true)
                .needThreadInfo(true)
                .globalTag("IS")
                .build()
        )
        PLog.prepare(DebugPrinter(BuildConfig.DEBUG)) //all logs will be automatically disabled on release version
    }
}