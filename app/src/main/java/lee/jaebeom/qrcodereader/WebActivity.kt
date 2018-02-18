package lee.jaebeom.qrcodereader

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.content.ContextCompat
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.activity_web.*

import android.view.WindowManager
import android.webkit.WebView
import android.widget.ProgressBar


class WebActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val name = intent.extras.getString("name")
        val url = intent.extras.getString("url")
        setContentView(R.layout.activity_web)
        toolbar.title = name
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        initWebView()

        if (savedInstanceState == null){
            web.loadUrl(url)
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(){
        val setting = web.settings
        web.webViewClient = WebViewClient()
        web.webChromeClient = WebViewChromeClient(this, progressBar)
        setting.javaScriptEnabled = true
        setting.mediaPlaybackRequiresUserGesture = true
        setting.allowFileAccess = true

    }

    override fun onBackPressed() {
        if (web.canGoBack()){
            web.goBack()
        }else{
            super.onBackPressed()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        web.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        web.restoreState(savedInstanceState)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
    class WebViewChromeClient(private val activity: Activity, private val progressBar: ProgressBar): WebChromeClient(){
        private var customView: View? = null
        private var fullScreenContainer: FrameLayout? = null
        private val COVER_SCREEN_PARAMS = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        private var originalOrientation: Int = 0
        private lateinit var customViewCallback: CustomViewCallback

        override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
            if (customView != null){
                callback?.onCustomViewHidden()
                return
            }
            originalOrientation = activity.requestedOrientation
            val decor: FrameLayout = activity.window.decorView as FrameLayout
            fullScreenContainer = FullScreenHolder(activity)
            fullScreenContainer?.addView(view, COVER_SCREEN_PARAMS)
            decor.addView(fullScreenContainer, COVER_SCREEN_PARAMS)
            customView = view
            setFullscreen(true)
            customViewCallback = callback!!

            super.onShowCustomView(view, callback)

        }

        @SuppressLint("WrongConstant")
        override fun onHideCustomView() {
            if (customView == null) return

            setFullscreen(false)
            val decor: FrameLayout = activity.window.decorView as FrameLayout
            decor.removeView(fullScreenContainer)
            fullScreenContainer = null
            customView = null
            customViewCallback.onCustomViewHidden()
            activity.requestedOrientation = originalOrientation
        }

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            progressBar.progress = newProgress
            if (newProgress == 100){
                progressBar.visibility = View.GONE
            }else{
                progressBar.visibility = View.VISIBLE
            }
        }

        private fun setFullscreen(enabled: Boolean) {
            val win = activity.window
            val winParams = win.attributes
            val bits = WindowManager.LayoutParams.FLAG_FULLSCREEN
            if (enabled) {
                winParams.flags = winParams.flags or bits
            } else {
                winParams.flags = winParams.flags and bits.inv()
                if (customView != null) {
                    customView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                }
            }
            win.attributes = winParams
        }

        class FullScreenHolder(context: Context) : FrameLayout(context){
            init {
                setBackgroundColor(ContextCompat.getColor(context, android.R.color.black))
            }

            override fun onTouchEvent(event: MotionEvent?): Boolean {
                return true
            }
        }
    }
}
