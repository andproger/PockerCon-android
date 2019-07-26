package by.aa.pockercon.presentation.app

import android.app.Application
import by.aa.pockercon.presentation.di.CustomProvider
import by.aa.pockercon.presentation.di.CustomProviderImpl

class App : Application() {

    private var provider: CustomProvider? = null

    fun getProvider(): CustomProvider {
        return provider ?: CustomProviderImpl(applicationContext).apply {
            provider = this
        }
    }

    override fun onTerminate() {
        provider?.dispose()
        provider = null

        super.onTerminate()
    }
}