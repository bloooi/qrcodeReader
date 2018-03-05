package lee.jaebeom.qrcodereader

import android.app.Application
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric



/**
 * Created by leejaebeom on 2018. 3. 5..
 */
class QRApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
    }

}