package lee.jaebeom.qrcodereader.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Created by leejaebeom on 2018. 2. 16..
 */

object SavePreference{
    //TODO: Singleton으로 context받는법 찾기
//        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    fun saveSharedPreference(context: Context, key: String, data: String){
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putString(key, data)
        editor.apply()
    }

    fun saveSharedPreference(context: Context, key: String, data: Int){
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putInt(key, data)
        editor.apply()
    }

    fun getStringPreference(context: Context, key: String) : String{
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(key, "")
    }
}