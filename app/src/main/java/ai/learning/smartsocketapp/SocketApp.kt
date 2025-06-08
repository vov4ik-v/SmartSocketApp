package ai.learning.smartsocketapp

import ai.learning.smartsocketapp.core.di.coreModule
import ai.learning.smartsocketapp.core.di.smartSocketModule
import ai.learning.smartsocketapp.core.di.testModule
import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext

class SocketApp : Application() {
    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            androidContext(this@SocketApp)
            modules(
                coreModule,
                testModule,
                smartSocketModule
            )
        }
    }
}