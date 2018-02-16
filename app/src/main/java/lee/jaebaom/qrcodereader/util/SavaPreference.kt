package lee.jaebaom.qrcodereader.util

import android.annotation.SuppressLint
import android.content.Context
import android.preference.PreferenceManager

/**
 * Created by leejaebeom on 2018. 2. 16..
 */

class SavaPreference{
    companion object {
        @SuppressLint("ApplySharedPref")
        fun saveShaerdPreference(context: Context, key: String, data: String){
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = prefs.edit()
            editor.putString(key, data)
            editor.commit()
            editor.apply()
        }

        fun saveShaerdPreference(context: Context, key: String, data: Int){
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = prefs.edit()
            editor.putInt(key, data)
            editor.commit()
            editor.apply()
        }

        fun getStringPreference(context: Context, key: String) : String{
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return prefs.getString(key, "")
        }
    }
}