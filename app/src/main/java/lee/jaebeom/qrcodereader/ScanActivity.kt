package lee.jaebeom.qrcodereader

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.journeyapps.barcodescanner.CaptureManager
import kotlinx.android.synthetic.main.activity_scan.*

class ScanActivity : AppCompatActivity() {
    private lateinit var manager: CaptureManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        scanner.viewFinder.visibility = View.INVISIBLE
        manager = CaptureManager(this, scanner)
        manager.initializeFromIntent(intent, savedInstanceState)
        manager.decode()
    }

    override fun onResume() {
        super.onResume()
        manager.onResume()
    }

    override fun onPause() {
        super.onPause()
        manager.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        manager.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        manager.onSaveInstanceState(outState)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return scanner.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }
}
